package s02;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;

public class MineHuntController {

  private MineHuntModel model;
  private MineHuntView   view;

  public MineHuntController(MineHuntModel model, MineHuntView view){
    this.model = model;
    this.view  = view;
  }


  public void leftClickAction(CellButton btn) {

    // si rien ou flagged, on ouvre
    if(!model.isOpen(btn.getRow(), btn.getCol()) || model.isFlagged(btn.getRow(), btn.getCol())){
      
      // ouvrir la cell
      if(model.open(btn.getRow(), btn.getCol())){
        
        // bombe
        
        // test if user is playing his first round 
        if(!model.isFirstRound()){
          btn.presentationOpenedOnMine(model.isSound());
        }else{
          
          // remove mine from model
          model.setMined(btn.getRow(), btn.getCol());
          
          // insert 1 mine in another position in the model
          model.randomMinesPlacement(1);
          
          // open the button
          int neighborMines = model.neighborMines(btn.getRow(), btn.getCol());
          btn.presentationOpened(neighborMines);
          if(neighborMines == 0){
            openProximity(btn.getRow(), btn.getCol());
          }
          
          // if showMines hide and display mines again
          if(view.isShowMines()){
            showMines(); showMines();
          }
          
          // set first round to false
          model.setFirstRound(false);
          
        }
        
      }else{
        
        // test if user is playing his first round
        if(model.isFirstRound()){
          model.setFirstRound(false);
        }
        
        // pas de bombe, affiche neighborMines
        int neighborMines = model.neighborMines(btn.getRow(), btn.getCol());
        btn.presentationOpened(neighborMines);

        // test si pas de neighborMines, découvre les cases à zéro à proximité
        if(neighborMines == 0){
          openProximity(btn.getRow(), btn.getCol());
        }

      }
    }
    else if(model.isOpen(btn.getRow(), btn.getCol())){
      // la cellule est deja ouverte on ne fait rien
    }

    // Update menu counters
    view.getClicks().setText(Integer.toString(model.clicks()));
    view.getErrors().setText(Integer.toString(model.errors()));

  }




  // Ouvre les mines à proximité d'une mine
  private void openProximity(int row, int col) {


    for(int i = row-1; i <= row+1; i++){
      for(int j = col-1; j <= col+1; j++){

        if((i == row) && (j == col)){
          // pas de test de la case elle-même
        }
        else if((i < 0) || (i > model.gridHeight()-1) || (j < 0) || (j > model.gridWidth()-1)){
          // pas de test dans les bordures
        }else{

          // si la mine à proximité a neighborMines à 0
          if(model.openProximity(i, j)){

            // ouverture de la case
            int neighborMines = model.neighborMines(i, j);

            // open button
            view.getGridCellButton()[j][i].presentationOpened(neighborMines);

            // appel récursif
            if(neighborMines == 0){
              openProximity(i, j);
            }

          }

        }

      } 
    }

  }




  public void rightClickAction(CellButton btn) {

    // flag
    if(!model.isOpen(btn.getRow(), btn.getCol()) && !model.isFlagged(btn.getRow(), btn.getCol())){

      btn.presentationFlagged();
      model.setFlagState(btn.getRow(), btn.getCol(), true);

    }else if(model.isFlagged(btn.getRow(), btn.getCol())){
      btn.presentationClosed();
      model.setFlagState(btn.getRow(), btn.getCol(), false);

    }

  }




  public void showMines(){

    for(int j = 0; j < model.gridHeight(); j++){
      for(int i = 0; i < model.gridWidth(); i++){
        if(model.isMined(j, i)){

          // get CellButton from position in CellButton grid
          CellButton minedButton = view.getGridCellButton()[i][j];    
          
          if(!view.isShowMines()){
            // modify button (display mines)
            minedButton.presentationShowMine();

          }else{
            // modify button (hide unopened mines only if not opened)
            if(!model.isOpen(j, i)){

              if(model.isFlagged(j, i)){
                minedButton.presentationFlagged();                              // button was flagged before
              }else{
                minedButton.presentationClosed();                               // button was opened before
              }

            }

          }

        }
      }
    }

    view.setShowMines(!view.isShowMines());

  }



  public void gameOver(){

    int errors = model.errors();

    if(errors > 0){
      gameOverAlert(AlertType.WARNING, false, errors);                          // Game lost
    }else{
      gameOverAlert(AlertType.INFORMATION, true, 0);                            // Game win
    }

  }




  private void gameOverAlert(AlertType alertType, boolean win, int errors){

    double endTime = System.currentTimeMillis();
    double gameTime = (endTime - view.getStartTime()) / 1000.0;
    String text = "";

    Alert dialog= new Alert(alertType);
    dialog.setTitle("MineHunt - GameOver");
    dialog.setHeaderText("MineHunt");

    if(win){
      text = "Congratulations !\nCurrent game ended successfully (no error)";
    }else{
      text = "Current game ended with " + errors + " errors";
    }
    text += "\n\nGame ended after " + Double.toString(gameTime) + " seconds";

    dialog.setContentText(text);
    dialog.showAndWait();

  }




  public void newGame() {

    // model
    int col = model.gridWidth();
    int row = model.gridHeight();
    int mines = model.mines(); 

    if(model.getNewGridCol() > 0) col = model.getNewGridCol();
    if(model.getNewGridRow() > 0) row = model.getNewGridRow();
    if(model.getNewMinesNb() > 0) mines = model.getNewMinesNb();

    model.newModel(col, row, mines);

    // view
    view.getGrid().getChildren().clear();
    view.gridButtonCreation();
    view.setupVariables();

  }




  public void settingsGridSize(){

    Dialog<String[]> dialog = new Dialog<String[]>();
    dialog.setTitle("Settings");
    dialog.setHeaderText("Configure grid size");

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    TextField row = new TextField();
    row.setText(String.valueOf(model.gridHeight()));
    TextField col = new TextField();
    col.setText(String.valueOf(model.gridWidth()));

    grid.add(new Label("Row :"), 0, 0);
    grid.add(row, 1, 0);
    grid.add(new Label("Col :"), 0, 1);
    grid.add(col, 1, 1);

    ButtonType okButtonType = new ButtonType("OK", ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
    dialog.getDialogPane().setContent(grid);

    dialog.setResultConverter(dialogButton -> {
      if (dialogButton == okButtonType) {
        return new String[]{row.getText(), col.getText()};
      }
      return null;
    });

    Optional<String[]> input = dialog.showAndWait();

    if(input.isPresent()){
      String newRowS = input.get()[0].toString();
      String newColS = input.get()[1].toString();
      if(isInteger(newRowS) && isInteger(newColS)){
        
        int newRow = Integer.valueOf(newRowS);
        int newCol = Integer.valueOf(newColS);
        int newSize = newRow * newCol;
        int mines = model.mines(); 
        if(model.getNewMinesNb() > 0) mines = model.getNewMinesNb();
        
        if(newRow > 0 && newCol > 0 && newSize >= mines ){
          
          model.setNewGridRow(Integer.valueOf(newRow));
          model.setNewGridCol(Integer.valueOf(newCol));
          
        }else{

          Alert alert = new Alert(AlertType.ERROR);
          alert.setTitle("Settings");
          alert.setHeaderText(null);
          alert.setContentText("Grid size parameters should be bigger than 0 and grid size should not be smaller than mines number !");
          alert.showAndWait();
          
        }

      }
    }

  }





  public void settingsMines(){

    TextInputDialog dialog = new TextInputDialog(Integer.toString(model.mines()));
    dialog.setTitle("Settings");
    dialog.setHeaderText("Configure number of mines");
    dialog.setGraphic(null);
    Optional<String> input = dialog.showAndWait();

    if(input.isPresent()){
      if(isInteger(input.get())){

        int row = model.gridHeight();
        int col = model.gridWidth();
        if(model.getNewGridCol() > 0) col = model.getNewGridCol();
        if(model.getNewGridRow() > 0) row = model.getNewGridRow();

        if(Integer.valueOf(input.get()) > 0 && Integer.valueOf(input.get()) <= (row * col)){
          model.setNewMinesNb(Integer.valueOf(input.get()));
        }else{

          Alert alert = new Alert(AlertType.ERROR);
          alert.setTitle("Settings");
          alert.setHeaderText(null);
          alert.setContentText("Mines number should be bigger than 0 and smaller than grid size !");
          alert.showAndWait();

        }

      }
    }


  }





  public void settingsStyle() {

    String[] choices = {"Modena", "Caspian"};
    ChoiceDialog<String> cDialog= new ChoiceDialog<>(choices[0], choices);
    cDialog.setTitle("Settings");
    cDialog.setHeaderText("Configure look and feel");
    cDialog.setGraphic(null);
    cDialog.setContentText("Look & Feel : ");
    Optional<String> selection = cDialog.showAndWait();

    if(selection.isPresent()){
      view.setLF(selection.get());
    }

  }





  private static boolean isInteger(String s) {
    try { 
      Integer.parseInt(s); 
    }catch(NumberFormatException e) { 
      return false; 
    }catch(NullPointerException e) {
      return false;
    }
    return true;
  }




}
