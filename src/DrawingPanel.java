import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class DrawingPanel extends JPanel {
    double t = 0;
    double theta = 0;
    int pointR = 40;
    int greppedPointIndex = -1;
    List<Point.Double> points = new ArrayList<>();

    public DrawingPanel() {
        super();
        points.add(new Point.Double(100, 100));
        points.add(new Point.Double(150, 150));
        points.add(new Point.Double(200, 200));
        points.add(new Point.Double(250, 250));

        points.add(new Point.Double(250, 250));
        points.add(new Point.Double(300, 300));
        points.add(new Point.Double(350, 350));
        points.add(new Point.Double(400, 350));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (greppedPointIndex != -1) {
                    greppedPointIndex = -1;
                    return;
                }
                for (int i = 0; i < points.size(); i++) {
                    if (e.getPoint().distance(points.get(i)) <= (pointR / 2)) {
                        greppedPointIndex = i;
                    }
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (greppedPointIndex == -1)
                    return;
                points.get(greppedPointIndex).setLocation(e.getPoint());
            }
        });
    }

    Point.Double interpolate(Point.Double p0, Point.Double p2) {
        return new Point.Double((1 - t) * p0.x + t * p2.x, (1 - t) * p0.y + t * p2.y);
    }

    @Override
    public void paint(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        Graphics2D graphics2d = (Graphics2D) g;
        graphics2d.clearRect(0, 0, width, height);
        drawGuide(graphics2d);

        for (int i = 0; i < points.size(); i++) {
            graphics2d.setColor(Color.CYAN);

            Point2D.Double point = points.get(i);
            if (i == greppedPointIndex)
                graphics2d.setColor(Color.MAGENTA);
            graphics2d.fillOval((int) point.getX() - (pointR / 2), (int) point.getY() - (pointR / 2), pointR, pointR);

        }

        for (int i = 0; i < points.size() - 3; i += 4) {
            Point2D.Double pI = points.get(i);
            Point2D.Double pIPlus1 = points.get(i + 1);
            Point2D.Double pIPlus2 = points.get(i + 2);
            Point2D.Double pIPlus3 = points.get(i + 3);

            Point2D.Double newInterpolated, newInterpolated12, newInterpolated23;
            Point2D.Double lastInterpolated = new Point.Double(0, 0);

            graphics2d.setColor(Color.PINK);
            graphics2d.drawLine(
                    (int) pI.x, (int) pI.y,
                    (int) pIPlus1.x, (int) pIPlus1.y);
            graphics2d.drawLine(
                    (int) pIPlus2.x, (int) pIPlus2.y,
                    (int) pIPlus3.x, (int) pIPlus3.y);

            graphics2d.setColor(Color.CYAN);
            for (t = 0; t < 1; t += .01) {
                Point2D.Double interpolatedI_IPlus1 = interpolate(pI, pIPlus1);
                Point2D.Double interpolatedIPlus1_IPlus2 = interpolate(pIPlus1, pIPlus2);
                Point2D.Double interpolatedIPlus2_IPlus3 = interpolate(pIPlus2, pIPlus3);

                newInterpolated12 = interpolate(interpolatedI_IPlus1, interpolatedIPlus1_IPlus2);
                newInterpolated23 = interpolate(interpolatedIPlus1_IPlus2, interpolatedIPlus2_IPlus3);
                newInterpolated = interpolate(newInterpolated12, newInterpolated23);

                if (t == 0)
                    lastInterpolated = newInterpolated;
                graphics2d.drawLine(
                        (int) lastInterpolated.x, (int) lastInterpolated.y,
                        (int) newInterpolated.x, (int) newInterpolated.y);
                lastInterpolated = newInterpolated;
            }

        }
        repaint();
    }

    void drawGuide(Graphics2D graphics2d) {
        graphics2d.setColor(Color.BLACK);
        graphics2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
        graphics2d.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
    }
}
