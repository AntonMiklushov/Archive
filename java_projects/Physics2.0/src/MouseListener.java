import com.jogamp.newt.event.MouseEvent;

public class MouseListener implements com.jogamp.newt.event.MouseListener {
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == 1) Main.pressed = true;
        if (mouseEvent.getButton() == 3) Particle.stop();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        Main.pressed = false;
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Main.fx = mouseEvent.getX();
        Main.fy = mouseEvent.getY();
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        Main.fx = mouseEvent.getX();
        Main.fy = mouseEvent.getY();
    }

    @Override
    public void mouseWheelMoved(MouseEvent mouseEvent) {

    }
}
