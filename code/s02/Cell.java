package s02;

public class Cell {
  
  private boolean open;
  private boolean flaged;
  private boolean mined;
  
  public Cell() {
    open = false;
    flaged = false;
    mined = false;
  }

  
  public boolean isOpen() {
    return open;
  }

  public void setOpen(boolean open) {
    this.open = open;
  }

  public boolean isFlaged() {
    return flaged;
  }

  public void setFlaged(boolean flaged) {
    this.flaged = flaged;
  }

  public boolean isMined() {
    return mined;
  }

  public void setMined(boolean mined) {
    this.mined = mined;
  }


}
