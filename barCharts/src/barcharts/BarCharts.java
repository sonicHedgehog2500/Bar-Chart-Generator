/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package barcharts;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

/**
 *
 * @author 15053003
 */
public class BarCharts extends Application {
    public Button genBarChart = new Button();   // Buttons
    public Button genCSV = new Button();
    
    public Label title = new Label("Title:");    // Labels
    public Label xAxis = new Label("X-Axis Label:");
    public Label yAxis = new Label("Y-Axis Label:");
    public Label itemName = new Label("Item Name");
    public Label value = new Label("Value");
    public Label enabled = new Label("Enabled");

    public TextField chartTitle, xLabel, yLabel;
    
    public HBox dataContainer, labelContainer;
    public VBox chartLabels, labelFields, buttons, item, itemValues, 
                enabledData, leftContainer, block;
    
    public int guiWidth = 670, guiHeight = 400, spacing = 10;   //GUI size and space between nodes
    
    Button[] buttonList = {genBarChart, genCSV};
    TextField[] headingFields = {chartTitle, xLabel, yLabel};
    TextField[] items = new TextField[10];
    TextField[] itemVal = new TextField[10];
    CheckBox[] enable = new CheckBox[10];
    
    CategoryAxis xDataItems; 
    NumberAxis yItemValues;
    XYChart.Series chartDataElements;
    BarChart<String, Number> newBarChart;
    
    public TextField[] createHeadingNameFields(){ //Creates textfields to input title and axis names
        String[] axisDescriptions = {" ", "Description of Items", "Description of Item Value"};
        for(int i = 0; i < headingFields.length; i++){
            headingFields[i] = new TextField();
            headingFields[i].setPromptText(axisDescriptions[i]);
        }
        return headingFields;
    }

    public Button[] createButtons(){ //Creates buttons and enables them to be formatted
        String[] buttonText = {"Generate Bar Chart", "Generate From CSV"};
        for(int i = 0; i < buttonList.length; i++){
            buttonList[i].setText(buttonText[i]);
            buttonList[i].setPrefWidth(230);
        }
        return buttonList;
    }
    
    public TextField[] createItemNameFields(){  //Creates textfields to enter data names
        for(int i = 0; i < items.length; i++){
            items[i] = new TextField();
            items[i].setPromptText("Item name");
        }
        return items;
    }
    
    public TextField[] createItemValues(){  //Creates textfields to enter data name values
        for(int i = 0; i < itemVal.length; i++){
            itemVal[i] = new TextField();
            itemVal[i].setPromptText("e.g. 50");
        }
        return itemVal;
    }
    
    public CheckBox[] createCheckBoxes(){   //Creates checkboxes to select data to display in chart
        for(int i = 0; i < enable.length; i++){
            enable[i] = new CheckBox();
        }
        return enable;
    }
   
    public GridPane leftPane(){ //Creates left hand side of the GUI
        chartLabels = new VBox(20); //Contains title and both axis labels
        chartLabels.getChildren().addAll(title, xAxis, yAxis);
        
        labelFields = new VBox(spacing); //Contains textFields for title and both axis 
        labelFields.getChildren().addAll(createHeadingNameFields());
        
        labelContainer = new HBox(spacing); //Contains 
        labelContainer.getChildren().addAll(chartLabels, labelFields);
        
        buttons = new VBox(spacing);
        buttons.getChildren().addAll(createButtons());
        
        dataContainer = new HBox(spacing);
        dataContainer.getChildren().addAll(chartLabels, labelFields);
        
        block = new VBox(); //Plain white square displayed in bottom left corner
        block.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        block.setPrefSize(50, 100);
        block.setAlignment(Pos.BOTTOM_LEFT);
        
        leftContainer = new VBox(spacing);//Container for content on left hand side
        leftContainer.getChildren().addAll(dataContainer, buttons);
        
        GridPane leftPane = new GridPane();
        leftPane.setPadding(new Insets(spacing));
        leftPane.setAlignment(Pos.TOP_LEFT);
        leftPane.setHgap(5);
        leftPane.setVgap(5);
        
        leftPane.add(leftContainer, 0, 0);
        leftPane.add(block, 0, 10);
        
        return leftPane;
    }
      
   @Override
    public void start(Stage primaryStage) {                
        genCSV.setOnMousePressed((MouseEvent generateCSVFileChart) -> { //Allows searching for file when genCSV button is pressed
            FileChooser file = new FileChooser();
            file.setTitle("Select file to generate bar chart");
            FileChooser.ExtensionFilter fileExtension = new FileChooser.ExtensionFilter("CSV", "*.csv");
            file.showOpenDialog(primaryStage);
        });
        
        genBarChart.setOnMouseClicked(new EventHandler<MouseEvent>() { //Creates new bar chart in a new window when genBarChart button is pressed 
            @Override
            public void handle(MouseEvent me) {
                xDataItems = new CategoryAxis(); 
                yItemValues = new NumberAxis();
                chartDataElements = new XYChart.Series();
                newBarChart = new BarChart<String, Number>(xDataItems, yItemValues);
                
                for(int i = 0; i < enable.length; i++){ //Gets data to be put in bar chart and convert them to required data types
                    if(enable[i].isSelected()){        // so they're displayed in bar chart 
                        enable[i].setSelected(true);
                        chartDataElements.getData().add(new XYChart.Data(items[i].getText(), Integer.parseInt(itemVal[i].getText())));
                    }
                }
                
                newBarChart.getData().addAll(chartDataElements);
                newBarChart.setLegendVisible(false);
                newBarChart.setTitle(headingFields[0].getText());                          
                xDataItems.setLabel(headingFields[1].getText());
                yItemValues.setLabel(headingFields[2].getText());
                
                Stage newStage = new Stage();
                Scene generatedChartScene = new Scene(newBarChart, guiWidth, guiHeight);
                newStage.setScene(generatedChartScene);
                newStage.setTitle(headingFields[0].getText());
                newStage.setResizable(false);
                newStage.show();
            }
        });

        item = new VBox(spacing);
        item.getChildren().add(itemName);
        item.getChildren().addAll(createItemNameFields());
        
        itemValues = new VBox(spacing);
        itemValues.getChildren().add(value);
        itemValues.getChildren().addAll(createItemValues());
        
        enabledData = new VBox(17);
        enabledData.getChildren().add(enabled);
        enabledData.getChildren().addAll(createCheckBoxes());
        
        GridPane chartData = new GridPane();
        chartData.setPadding(new Insets(spacing));
        chartData.setAlignment(Pos.TOP_LEFT);
        chartData.setHgap(5);
        chartData.setVgap(5);
        
        chartData.add(leftPane(), 0, 0);
        chartData.add(item, 4, 0);
        chartData.add(itemValues, 8, 0);
        chartData.add(enabledData, 12, 0);
              
        Scene scene = new Scene(chartData, guiWidth, guiHeight);
        primaryStage.setTitle("Bar Chart Generator");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();   
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }    
}
