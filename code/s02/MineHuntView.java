package s02;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class MineHuntView extends Application{

  private static final String TITLE = "/resources/MineHunt_Title.png";
  private static final String ICON  = "/resources/MineHunt_Bomb.png";

  private BorderPane    root            = new BorderPane();
  private VBox          title           = new VBox(20);
  private HBox          counters        = new HBox(10);
  private GridPane      grid            = new GridPane();
  private HBox          controls        = new HBox(20);
  private MenuBar       menu            = new MenuBar();

  private ImageView     titleImage      = new ImageView(TITLE);
  private Label         clicksLabel     = new Label("Nb clicks:");
  private Label         clicks          = new Label();
  private Label         errorsLabel     = new Label("Nb errors:");
  private Label         errors          = new Label();

  private Button        btnShowMines    = new Button("Show Mines");
  private Button        btnNewGame      = new Button("New Game");

  private MineHuntModel      model      = new MineHuntModel();
  private MineHuntController controller = new MineHuntController(model, this);
  private CellButton[][]     gridCellButton;
  
  private boolean       showMines;
  private double        startTime;


  @Override
  public void start(Stage mainStage) throws Exception {
    
    // Stage
    mainStage.setTitle("MineHunt v1.0");
    mainStage.setMinWidth(550);
    mainStage.setResizable(true);
    mainStage.getIcons().add(new Image(ICON));

    // BorderPane
    root.setTop(title);
    root.setCenter(grid);
    root.setBottom(controls);

    // Background
    Background background = new Background(new BackgroundFill(Color.rgb(255, 255, 204), CornerRadii.EMPTY, null));
    root.setBackground(background);

    
    // -------------------------------------------------------
    
    
    // Menu about
    Menu menuAbout = new Menu();
    Label lblMenuAbout = new Label("About");
    menuAbout.setGraphic(lblMenuAbout);
    
    lblMenuAbout.setOnMouseClicked(event-> {
      
      Alert dialog= new Alert(AlertType.INFORMATION);
      dialog.setTitle("About");
      dialog.setHeaderText("MineHunt Application");
      dialog.setContentText("Version : 1.0\n" + "Date : 2015-04-21\n\n" + "Gilles Waeber & Benoit Repond\n" + "HEIA-FR IHM");
      dialog.showAndWait();
      
    });
    
    
    // Menu settings
    Menu menuSettings = new Menu("Settings");
    MenuItem setGridSize = new MenuItem("Grid size");
    MenuItem setMinesNb = new MenuItem("Mines number");
    MenuItem setStyle = new MenuItem("Change style");
    CheckMenuItem setSound = new CheckMenuItem("Sound");
    setSound.setSelected(true);
    menuSettings.getItems().addAll(setGridSize, setMinesNb, setStyle, setSound);
    
    setGridSize.setOnAction(event-> {
      controller.settingsGridSize();
    });
    
    setMinesNb.setOnAction(event-> {
      controller.settingsMines();
    });
    
    setStyle.setOnAction(event -> {
      controller.settingsStyle();
    });
    
    setSound.setOnAction(event-> {
      model.setSound(!model.isSound());
    });
    

    // Menu
    menu.getMenus().addAll(menuSettings, menuAbout);
    title.getChildren().add(menu);
    

    // -------------------------------------------------------


    // Title image
    title.setAlignment(Pos.CENTER);
    title.getChildren().add(titleImage);

    // Number clicks
    clicks.setAlignment(Pos.BASELINE_RIGHT);
    clicks.setPrefWidth(80);
    clicks.setPadding(new Insets(10, 10, 10, 10));
    clicks.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5), null)));

    // Number errors
    errors.setAlignment(Pos.BASELINE_RIGHT);
    errors.setPrefWidth(80);
    errors.setPadding(new Insets(10, 10, 10, 10));
    errors.setBackground(new Background(new BackgroundFill(Color.LIGHTSALMON, new CornerRadii(5), null)));
    errorsLabel.setPadding(new Insets(0, 0, 0, 50));

    // Title
    counters.setAlignment(Pos.CENTER);
    counters.getChildren().add(clicksLabel);
    counters.getChildren().add(clicks);
    counters.getChildren().add(errorsLabel);
    counters.getChildren().add(errors);
    title.getChildren().add(counters);
    title.setPadding(new Insets(0, 0, 20, 0));


    // -------------------------------------------------------


    // Grid
    grid.setAlignment(Pos.CENTER);
    grid.setHgap(5);
    grid.setVgap(5);
    
    // Button grid creation
    gridButtonCreation();

    // -------------------------------------------------------


    // Controls
    btnNewGame.setDefaultButton(true);
    controls.setAlignment(Pos.BASELINE_RIGHT);
    controls.getChildren().add(btnShowMines);
    controls.getChildren().add(btnNewGame);
    controls.setPadding(new Insets(20, 25, 20, 10));

    // NewGame event handler
    btnNewGame.setOnAction(event -> {
      controller.newGame();
    });

    // ShowMines event handler
    btnShowMines.setOnAction(event -> {
      controller.showMines();
    });


    // -------------------------------------------------------

    // Misc
    setupVariables();
    
    // App launch
    mainStage.setScene(new Scene(root));
    mainStage.show();

  }
  
  
  
  public void gridButtonCreation() {
    
    gridCellButton = new CellButton[model.gridWidth()][model.gridHeight()];
    
    for(int j = 0; j < model.gridHeight(); j++){
      for(int i = 0; i < model.gridWidth(); i++){

        CellButton btn = new CellButton(j, i);
        btn.setMinSize(35, 35);
        
        gridCellButton[i][j] = btn;
        grid.add(gridCellButton[i][j], i, j);

        // Button event handler
        btn.setOnMouseClicked(event -> {

          // Check if game is not already ended
          if(!model.isGameOver()){
            
            // Handle left or right mouse click
            if(event.getButton() == MouseButton.PRIMARY){
              controller.leftClickAction(btn);
            }else if(event.getButton() == MouseButton.SECONDARY){
              controller.rightClickAction(btn);
            }
            
            // Check if game is over
            if(model.isGameOver()){
              controller.gameOver();
            }
            
          }

        });

      }
    }
    
    
    
  }


  
  public void setupVariables() {
    errors.setText("0");
    clicks.setText("0");
    startTime = System.currentTimeMillis();
    showMines = false;
  }
  
  
  
  // Change Look and Feel
  public void setLF(String style){
    
    switch (style) {
      case "Modena" :
        setUserAgentStylesheet(STYLESHEET_MODENA);
        break;
      case "Caspian" :
        setUserAgentStylesheet(STYLESHEET_CASPIAN);
        break;
      default :
        setUserAgentStylesheet(STYLESHEET_MODENA);
        break;
    }
    
  }


  
  // Getters and setters
  boolean isShowMines() {
    return showMines;
  }

  void setShowMines(boolean showMines) {
    this.showMines = showMines;
  }

  double getStartTime() {
    return startTime;
  }
  
  Label getClicks() {
    return clicks;
  }
  
  Label getErrors(){
    return errors;
  }
  
  GridPane getGrid(){
    return grid;
  }

  CellButton[][] getGridCellButton(){
    return gridCellButton;
  }
  
  
  /*
  @Override
  public void init() throws Exception{

  }*/


}
