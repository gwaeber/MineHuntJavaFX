package s02;

public interface IMineHuntModel {
  
  /**
   * Initialize a new game
   * Reset game settings (counters and new values)
   * Random mines placement
   * 
   * @param minesNb : Number of mines
   */
  void initNewGame(int minesNb);
  
  /**
   * Number of columns in grid
   */
  int gridWidth();
  
  /**
   * Number of rows in grid
   */
  int gridHeight();
  
  /**
   * Number of mines in grid
   */
  int mines();
  
  /**
   * Error counter
   */
  int errors();
  
  /**
   * Clicks counter
   */
  int clicks();
  
  /**
   * Number of mines in the neighborhood of a cell
   * 
   * @param row Cell row index
   * @param col Cell column index
   */
  int neighborMines(int row, int col);
  
  /**
   * Return true if cell is open
   * 
   * @param row : Cell row index
   * @param col : Cell column index
   */
  boolean isOpen(int row, int col);
  
  /**
   * Return true if cell is flagged
   * 
   * @param row : Cell row index
   * @param col : Cell column index
   */
  boolean isFlagged(int row, int col);
  
  /**
   * Return true if cell is mined
   * 
   * @param row : Cell row index
   * @param col : Cell column index
   */
  boolean isMined(int row, int col);
  
  /**
   * Check if game is over
   * Return true only if all non-mined cells have been opened
   * 
   * @return true if game over
   */
  boolean isGameOver();
  
  /**
   * Open a cell
   * No effect if cell is already open.
   * 
   * @param  row   Cell row index
   * @param  col   Cell column index
   * @return true if a mine was in the cell
   */
  boolean open(int row, int col);
  
  /**
   * Set cell to flagged
   * 
   * @param row   : Cell row index
   * @param col   : Cell column index
   * @param state : flag state
   */
  void setFlagState(int row, int col, boolean state);
  
  
  /**
   * Open a cell (used when user click on cell with no neighbor mines)
   * No effect if cell is already open or if there is a bomb
   * 
   * @param row   : Cell row index
   * @param col   : Cell column index
   */
  boolean openProximity(int row, int col);
  
  
  /**
   * Generate a new model (used to start a new game)
   *
   * @param gridSizeCol : col
   * @param gridSizeRow : row
   * @param minesNb     : Number of mines
   */
  void newModel(int gridSizeCol, int gridSizeRow, int minesNb);
  
}
