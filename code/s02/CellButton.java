package s02;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

public class CellButton extends Button{

  private static final String FLAGG      = "/resources/MineHunt_Flagg.png";
  private static final String BOMB       = "/resources/MineHunt_Bomb.png";
  private static final String BOMB_SOUND = "/resources/MineHunt_Bomb_Explosion.mp3";
  
  private int row;
  private int col;
  
  private Media sound = new Media(getClass().getResource(BOMB_SOUND).toString());
  private MediaPlayer mediaPlayer = new MediaPlayer(sound);

  public CellButton(int row, int col) {
    this.row = row;
    this.col = col;
  }
  
  
  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }
  
  public void presentationClosed(){
    setGraphic(null);
  }
  
  // Open a cell
  public void presentationOpened(int nbrBombs){
    setGraphic(null);
    setBackground(new Background(new BackgroundFill(Color.rgb(250, 250, 0), new CornerRadii(5), null)));
    if(nbrBombs != 0){
      setText(Integer.toString(nbrBombs));
    }
  }

  // Mark cell as flagged
  public void presentationFlagged(){
    setGraphic(new ImageView(FLAGG));
    setPadding(new Insets(0, 0, 0, 0));
  }

  // Open a mined cell
  public void presentationOpenedOnMine(boolean sound){
    setGraphic(new ImageView(BOMB));
    setPadding(new Insets(0, 0, 0, 0));
    setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(5), null)));
    if(sound){
      mediaPlayer.play();
    }
    
  }
  
  // Show mines
  public void presentationShowMine(){
    setGraphic(new ImageView(BOMB));
    setPadding(new Insets(0, 0, 0, 0));
  }


}
