import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Donut extends JPanel {
    private BufferedImage buffer;
    private int width = 800;
    private int height = 600;
    private double angleX = 0;
    private double angleY = 0;
    private double angleZ = 0;
    private double lightX = 0.5, lightY = 1, lightZ = 1;

    public Donut() {
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderdonut();
        g.drawImage(buffer, 0, 0, this);
    }

    private void renderdonut() {
        Graphics2D g2d = buffer.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);

        int radius = 150;
        int centerX = width / 2;
        int centerY = height / 2;

        // 3D rotation matrix
        double cosX = Math.cos(angleX);
        double sinX = Math.sin(angleX);
        double cosY = Math.cos(angleY);
        double sinY = Math.sin(angleY);

        // Create the donut
        for (int i = 0; i < 360; i += 5) {
            for (int j = 0; j < 360; j += 5) {
                // Parametric equations for a torus
                double x = (radius + Math.cos(Math.toRadians(j)) * 40) * Math.cos(Math.toRadians(i));
                double y = (radius + Math.cos(Math.toRadians(j)) * 40) * Math.sin(Math.toRadians(i));
                double z = Math.sin(Math.toRadians(j)) * 40;

                // Apply rotation on all axes
                double tempX = x * cosY - z * sinY;
                double tempZ = x * sinY + z * cosY;
                x = tempX;
                z = tempZ;

                double tempY = y * cosX - z * sinX;
                z = y * sinX + z * cosX;
                y = tempY;

                // Lighting calculation for shading
                double dotProduct = lightX * x + lightY * y + lightZ * z;
                double magnitude = Math.sqrt(x * x + y * y + z * z);
                double lightIntensity = Math.max(0, dotProduct / (magnitude * Math.sqrt(lightX * lightX + lightY * lightY + lightZ * lightZ)));

                // Convert 3D to 2D
                int screenX = (int) (centerX + x);
                int screenY = (int) (centerY - y);

                // Texture effect: Gradients based on angle and position
                int color = (int) (255 * lightIntensity);
                g2d.setColor(new Color(color, color, (int) (255 * (1 - lightIntensity))));

                // Draw the point
                g2d.fillOval(screenX, screenY, 4, 4);
            }
        }

        // Update the angles for smooth animation
        angleX += 0.03;
        angleY += 0.03;
        angleZ += 0.03;

        g2d.dispose();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Realistic 3D Spinning donut with Texture");
        Donut panel = new Donut();
        frame.add(panel);
        frame.setSize(panel.width, panel.height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        while (true) {
            panel.repaint();
            try {
                Thread.sleep(20); // Faster for smoother animation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

