/*  Created by Connor Rowland
    Apr 5, 2017
*/

package audioviz;

import static java.lang.Integer.min;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CarfzfSuperVisualizer implements Visualizer {
    
    private final String name = "CarfzfSuperVisualizer";
    
    private String vizPaneInitialStyle = "";
    private Integer numBands;
    private AnchorPane vizPane;
    private Double width = 0.0;
    private Double height = 0.0;
    private Double bandWidth = 0.0;
    private Double bandHeight;
    private Rectangle[] rectangles;
    private final Double startHue = 120.0;
    private Double percHeight;
    private Double newColor;
    

    @Override
    public void start(Integer numBands, AnchorPane vizPane) {
        end();
        
        vizPane.setStyle("-fx-background-color: black");
        
        vizPaneInitialStyle = vizPane.getStyle();
        this.numBands = numBands;
        this.vizPane = vizPane;
        
        height = vizPane.getHeight();
        width = vizPane.getWidth();
        
        Rectangle clip = new Rectangle(width, height);
        clip.setLayoutX(0);
        clip.setLayoutY(0);
        vizPane.setClip(clip);
        
        bandWidth = width / numBands;
        bandHeight = height * 1.3;
        rectangles = new Rectangle[numBands];
        for(int i = 0; i < numBands; i++) {
            Rectangle rectangle = new Rectangle();
            
            rectangle.setX(bandWidth * i);
            rectangle.setY(height - 25);
            
            rectangle.setWidth(bandWidth);
            rectangle.setHeight(3);
            
            rectangle.setFill(Color.hsb(startHue, 1.0, 1.0, 1.0));
            vizPane.getChildren().add(rectangle);
            rectangles[i] = rectangle;
        }
        
    }

    @Override
    public void end() {
        if (rectangles != null) {
            for (Rectangle rectangle : rectangles) {
                vizPane.getChildren().remove(rectangle);
            }
            rectangles = null;
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
        if(rectangles == null){
            return;
        }
        
        Integer num = min(rectangles.length, magnitudes.length);
        
        for(int i = 0; i < num; i++) {
            
            
            if((height - (((60.0 + magnitudes[i]) / 60.0) * (bandHeight) + 25)) < 0)
                rectangles[i].setY(0);
            else if((height - (((60.0 + magnitudes[i]) / 60.0) * (bandHeight) + 25)) < rectangles[i].getY())
                rectangles[i].setY(height - (((60.0 + magnitudes[i]) / 60.0) * (bandHeight) + 25));
            else if(rectangles[i].getY() < (height-28))
                rectangles[i].setY(rectangles[i].getY() + 3);
            else
                rectangles[i].setY(height - 25);
            
            rectangles[i].setHeight(Math.sqrt(height - rectangles[i].getY()));
                
            
            percHeight = (Math.pow(rectangles[i].getHeight(), 2)) / (height);
            if(percHeight > 1)
                percHeight = 1.0;
            newColor = 120 - (percHeight * 120);
            rectangles[i].setFill(Color.hsb(newColor, 1.0, 1.0, 1.0));
        }
    }
    
}
