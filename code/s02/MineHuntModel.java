package s02;

import java.util.Random;

public class MineHuntModel implements IMineHuntModel{

  private int minesNb;
  private int clickNb;
  private int errorsNb;
  private Cell[][] grid;
  
  private boolean sound = true;
  private boolean firstRound = true;
  
  private int newMinesNb = 0;
  private int newGridCol = 0;
  private int newGridRow = 0;

  public MineHuntModel(int gridSizeCol, int gridSizeRow, int minesNb){
    this.minesNb = minesNb;
    buildGrid(gridSizeCol, gridSizeRow);
    initNewGame(minesNb);
  }

  public MineHuntModel(){
    this(10, 10, 18);
  }
  
  
  // Build model grid
  private void buildGrid(int gridSizeCol, int gridSizeRow){
    
    grid = new Cell[gridSizeCol][gridSizeRow];
    for(int i = 0; i < gridSizeCol; i++){
      for(int j = 0; j < gridSizeRow; j++){
        grid[i][j] = new Cell();
      }
    }
    
  }


  @Override
  public void initNewGame(int minesNb) {

    // Random mines placement in the grid
    randomMinesPlacement(minesNb);
    
    // Counters init
    clickNb = 0;
    errorsNb = 0;
    
    firstRound = true;
    
    setNewMinesNb(0);
    setNewGridCol(0);
    setNewGridRow(0);

  }
  

  void randomMinesPlacement(int minesNb){
    
    Random r = new Random();
    for(int i = 0; i < minesNb; i++){

      int col = r.nextInt(gridWidth());
      int row = r.nextInt(gridHeight());

      if(!grid[col][row].isMined()){
        grid[col][row].setMined(true);
      }else{
        i--;    // mine already exist here
      }

    }
    
  }


  @Override
  public int gridWidth() {
    return grid.length;
  }


  @Override
  public int gridHeight() {
    return grid[0].length;
  }

  @Override
  public int mines() {
    return minesNb;
  }


  @Override
  public int errors() {
    return errorsNb;
  }

  @Override
  public int clicks(){
    return clickNb;
  }


  @Override
  public int neighborMines(int row, int col) {

    int neighborMines = 0;

    for(int i = row-1; i <= row+1; i++){
      for(int j = col-1; j <= col+1; j++){

        if((i == row) && (j == col)){
          // no test in the cell itself
        }
        else if((i < 0) || (i > gridHeight()-1) || (j < 0) || (j > gridWidth()-1)){
          // no test in the borders
        }else{
          if(grid[j][i].isMined()){
            neighborMines++;
          }

        }

      } 
    }

    return neighborMines;
    
  }


  @Override
  public boolean isOpen(int row, int col) {
    return grid[col][row].isOpen();
  }


  @Override
  public boolean isFlagged(int row, int col) {
    return grid[col][row].isFlaged();
  }

  @Override
  public boolean isMined(int row, int col) {
    return grid[col][row].isMined();
  }
  
  public void setMined(int row, int col){
    grid[col][row].setMined(false);
  }


  @Override
  public boolean isGameOver() {
    
    for(int j = 0; j < gridHeight(); j++){
      for(int i = 0; i < gridWidth(); i++){

        if(!isMined(j, i)){
          if(!isOpen(j, i)){
            return false;
          }
        }

      }
    }
    
    return true;
  }


  @Override
  public boolean open(int row, int col) {

    Cell cell = grid[col][row];

    if(!cell.isOpen()){

      cell.setOpen(true);
      cell.setFlaged(false);
      clickNb++;

      if(cell.isMined()){
        if(!firstRound){
          errorsNb++;
        }
        return true;
      }else{
        return false;
      }

    }

    return false;

  }
  

  @Override
  public void setFlagState(int row, int col, boolean state) {
    grid[col][row].setFlaged(state);
  }
  
  
  
  @Override
  public boolean openProximity(int row, int col) {

    Cell cell = grid[col][row];

    if(!cell.isOpen() && !cell.isMined()){

      cell.setOpen(true);
      cell.setFlaged(false);
      return true;

    }

    return false;
    
  }
  
  
  
  
  @Override
  public void newModel(int gridSizeCol, int gridSizeRow, int minesNb){
    
    this.minesNb = minesNb;
    grid = null;
    buildGrid(gridSizeCol, gridSizeRow);
    initNewGame(minesNb);
    
  }

  
  
  
  boolean isSound() {
    return sound;
  }

  void setSound(boolean sound) {
    this.sound = sound;
  }

  int getNewMinesNb() {
    return newMinesNb;
  }

  void setNewMinesNb(int newMinesNb) {
    this.newMinesNb = newMinesNb;
  }

  int getNewGridCol() {
    return newGridCol;
  }

  void setNewGridCol(int newGridCol) {
    this.newGridCol = newGridCol;
  }

  int getNewGridRow() {
    return newGridRow;
  }

  void setNewGridRow(int newGridRow) {
    this.newGridRow = newGridRow;
  }
  
  boolean isFirstRound(){
    return firstRound;
  }
  
  void setFirstRound(boolean firstRound){
    this.firstRound = firstRound;
  }
  

}
