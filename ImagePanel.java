/*
  ______ __  __          __  __ ________   _____   ___
 |  ____|  \/  |   /\   |  \/  |  ____\ \ / / _ \ / _ \
 | |__  | \  / |  /  \  | \  / | |__   \ V / (_) | (_) |
 |  __| | |\/| | / /\ \ | |\/| |  __|   > < \__, |> _ <
 | |____| |  | |/ ____ \| |  | | |____ / . \  / /| (_) |
 |______|_|  |_/_/    \_\_|  |_|______/_/ \_\/_/  \___/

Emanuel Estrada Larios - A01633605
*/

import javax.swing.*;
import java.awt.*;
import java.awt.Graphics.*;
import java.awt.event.*;

import java.io.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.*;
import java.net.*;
import java.awt.Graphics;

class ImagePanel extends JPanel {

  private BufferedImage inputImg;

  public ImagePanel(){
    super();

    this.setPreferredSize(new Dimension(500,300));

    this.inputImg = null;
  }

  public void paintComponent(Graphics g){
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g;

    if(this.inputImg != null)
      g2.drawImage(this.inputImg,5,5,490,290,null);

  }

  public void updatePanel(File img){
    try{
      this.inputImg = ImageIO.read(img);
      this.repaint();
    }
    catch (IOException e) {  System.out.println("Error"); }
  }

}
