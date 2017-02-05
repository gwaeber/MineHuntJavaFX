package s02.test;

import s02.MineHuntModel;

public class MineHuntModelTest {

  public static void main(String[] args) {


    MineHuntModel mhm = new MineHuntModel(10, 6, 13);

    mhm.setFlagState(0, 0, true);
    //mhm.grid[8][0].setFlaged(true);
    mhm.open(2, 4);
    //mhm.grid[3][1].setOpen(true);

    /*
    for(int j = 0; j < mhm.gridHeight(); j++){
      for(int i = 0; i < mhm.gridWidth(); i++){
        mhm.open(j, i);
      }
    }
    */

    
    // affichage grille
    /*
    System.out.println("Tableau de " + mhm.gridHeight() + " sur " + mhm.gridWidth() + " avec " + mhm.mines() + " mines.");
    for(int j = 0; j < mhm.gridHeight(); j++){
      for(int i = 0; i < mhm.gridWidth(); i++){

        if(mhm.grid[i][j].isOpen()){
          System.out.print("O  ");
        }else if(mhm.grid[i][j].isFlaged()){
          System.out.print("F  ");
        }else if(mhm.grid[i][j].isMined()){
          System.out.print("x  ");
        }else{
          System.out.print("-  ");
        }


      }
      System.out.println("");
    }
    */
    
    

    System.out.println();
    System.out.println("Mine près de 1:1  = " + mhm.neighborMines(1, 1));
    System.out.println("Mine près de 3:3  = " + mhm.neighborMines(3, 3));
    System.out.println("Mine près de 6:10 = " + mhm.neighborMines(6, 10));
    System.out.println("Game over ? : " + mhm.isGameOver());

  }

}
