package audioviz;

import static java.lang.Integer.min;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

/**
 *
 * @author carfzf
 */
public class CarfzfCoolVisualizer implements Visualizer{

    private final String name = "CarfzfCoolVisualizer";
    
    private String vizPaneInitialStyle = "";
    private Integer numBands;
    private AnchorPane vizPane;
    private Double width = 0.0;
    private Double height = 0.0;
    private Double bandWidth = 0.0;
    private Ellipse[] ellipses;
    private final Double startHue = 260.0;
    private final Double pi = 3.14159;
    
    @Override
    public void start(Integer numBands, AnchorPane vizPane) {
        end();
        
        vizPane.setStyle("-fx-background-color: black");
        
        vizPaneInitialStyle = vizPane.getStyle();
        this.numBands = numBands;
        this.vizPane = vizPane;
        
        height = vizPane.getHeight();
        width = vizPane.getWidth();
        
        bandWidth = width / numBands;
        ellipses = new Ellipse[numBands];
        for(int i = 0; i < numBands; i++){
            Ellipse ellipse = new Ellipse();
            ellipse.setCenterX(bandWidth / 2 + bandWidth * (numBands/2));
            ellipse.setCenterY(height/2);
            
            ellipse.setRadiusX(40);
            ellipse.setRadiusY(40);
            
            ellipse.setFill(Color.hsb(startHue, 1.0, 1.0, 1.0));
            ellipse.toFront();
            vizPane.getChildren().add(ellipse);
            ellipses[i] = ellipse;
        }
        
    }

    @Override
    public void end() {
        if (ellipses != null) {
            for (Ellipse ellipse : ellipses) {
                vizPane.getChildren().remove(ellipse);
            }
            ellipses = null;
            vizPane.setClip(null);
            vizPane.setStyle(vizPaneInitialStyle);
        }    
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void update(double timestamp, double duration, float[] magnitudes, float[] phases) {
        if(ellipses == null){
            return;
        }
        
        Integer num = min(ellipses.length, magnitudes.length);
        
        for(int i = 0; i < num; i++){
            ellipses[i].setFill(Color.hsb(startHue - (magnitudes[i]*-6.0), 1.0, 1.0, 1.0));
            
            ellipses[i].setTranslateX((-Math.cos(2.5*pi / numBands * i)) * ((Math.sqrt(Math.sqrt(Math.sqrt(Math.abs(magnitudes[i]) * 5)))) - 2.04) * 600 );
            ellipses[i].setTranslateY(( Math.sin(2.5*pi / numBands * i)) * ((Math.sqrt(Math.sqrt(Math.sqrt(Math.abs(magnitudes[i]) * 5)))) - 2.04) * 600 );
            ellipses[i].setOpacity((Math.abs(magnitudes[i]) % 100) / 100);
        }
    }
    
}
