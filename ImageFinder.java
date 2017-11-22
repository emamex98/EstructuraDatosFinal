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
import java.awt.event.*;
import java.io.*;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.*;
import java.net.*;
import javax.swing.filechooser.*;

class ImageFinder extends JPanel implements ActionListener{

  private LinearProbeHash finder;
  private File inputImg, matchImg;

  private JTextField tfImagen;
  private JButton btExaminar, btAdd, btSearch, btDlt;

  private ImagePanel display;

  public ImageFinder(ImagePanel display){
    super();
    this.setPreferredSize(new Dimension(500,300));

    this.finder = new LinearProbeHash(100);
    this.display = display;

    this.add(new JLabel("<html><center><br><b>Ruleta 4<br>Emanuel Estrada Larios<br>Linear Probing Hash</b><br><br>Problema: Los fot&oacute;grafos que toman fotos en eventos<br>buscan formas r&aacute;pidas de encontrar im&aacute; duplicadas<br>antes de vender sus paquetes de fotograf&iacute;cos.<br><br>Seleccione una imagen:<br><br></center></html>"));

    this.tfImagen = new JTextField(25);
    this.add(this.tfImagen);

    this.btExaminar = new JButton("Examinar...");
    this.btExaminar.addActionListener(this);
    this.add(this.btExaminar);

    this.btAdd = new JButton("Agregar");
    this.btAdd.addActionListener(this);
    this.add(this.btAdd);

    this.btSearch = new JButton("Buscar");
    this.btSearch.addActionListener(this);
    this.add(this.btSearch);

    this.btDlt = new JButton("Eliminar");
    this.btDlt.addActionListener(this);
    this.add(this.btDlt);

  }
  
  public void contentFolder(String f){
	  File directory=new File(f);
	  File[] cOF=directory.listFiles();
	  for(File object: cOF){
		  addImage(object);
	  }
  }

  //////////////////////////////////////////////////////////

  public void actionPerformed(ActionEvent e){

    if(e.getSource() == this.btExaminar){
      JFileChooser chooser = new JFileChooser();
      
      FileNameExtensionFilter filter = new FileNameExtensionFilter( "Imagenes", "jpg", "gif", "jpeg", "png");
      
      chooser.setFileFilter(filter);
      int returnVal = chooser.showOpenDialog(this);

      if(returnVal == JFileChooser.APPROVE_OPTION) {
    	  this.inputImg = chooser.getSelectedFile();
          this.tfImagen.setText(this.inputImg.getName());
          this.addImage(this.inputImg);
          this.display.updatePanel(this.inputImg);
          this.revalidate();
      }
    }

    else if(e.getSource() == this.btAdd){
    	JFileChooser chooser=new JFileChooser();
    	chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	int rV = chooser.showOpenDialog(this);
    	if(rV== JFileChooser.APPROVE_OPTION){
    		String rA = chooser.getSelectedFile().toString();
    		this.contentFolder(rA);
    	}
    }

    else if(e.getSource() == this.btSearch){
      int[] result = this.searchImage(this.inputImg);
    }

    else if(e.getSource() == this.btDlt)
      this.deleteImage(this.inputImg);
      this.display.updatePanel(null);
  }

  ///////////////////////////////////////////////////////////

  private int imgKeyGen (File img) {

    BufferedImage originalImg = null;
    int pxAvobe = 0, pxBelow = 0;

    try {
      originalImg = ImageIO.read(img);

      Image thumbnail = originalImg.getScaledInstance(8, 8, Image.SCALE_SMOOTH);

      BufferedImage bufferedThumbnail = new BufferedImage(thumbnail.getWidth(null), thumbnail.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);

      bufferedThumbnail.getGraphics().drawImage(thumbnail, 0, 0, null);

      int[] pixels = bufferedThumbnail.getRGB(0, 0, 8, 8, null, 0, 8);
      int colourAverage = 0;

      for (int i=0; i<pixels.length; i++) {
        colourAverage += pixels[i];
      }

      colourAverage = colourAverage / 64;

      for (int i=0; i<pixels.length; i++) {
        if(pixels[i] <= colourAverage)
          pxBelow++;
        else
          pxAvobe++;
      }

    }
    catch (IOException e) {  System.out.println("Error"); }
    return Integer.parseInt("" + pxBelow + pxAvobe);
  }

  //////////////////////////////////////////////////////////

  public void addImage(File img){
    Key newKey = new Key(imgKeyGen(img),img.getPath());
    finder.insertElement(newKey);
    int[] index = finder.searchElement(newKey);
    JOptionPane.showMessageDialog(null,"La imagen fue agregada exitosamente. Llave #" + imgKeyGen(img) + " - Indice: #" + index[0]);
  }

  //////////////////////////////////////////////////////////

  public int[] searchImage(File img){
    int[] k = finder.searchElement(new Key(imgKeyGen(img),img.getPath()));
    if(k.length == 1){
      JOptionPane.showMessageDialog(null, "Imagen encontrada en indice " + k[0] + ", llave #" + imgKeyGen(img));
      return k;
    }
    else {
      String similarIndexes = "";
      for (int j = 0; j<k.length; j++){
        if(k[j] != 0)
          similarIndexes += k[j] + ", ";
      }
      JOptionPane.showMessageDialog(null, "No se encontro la imagen, imagenes similares en " + similarIndexes);
      return k;
    }
  }

  ////////////////////////////////////////////////////////////

  public void deleteImage(File img){
    Key imgK = new Key(imgKeyGen(img),img.getPath());
    int[] k = finder.searchElement(imgK);
    if(k.length == 1){
      finder.deleteElement(imgK);
      JOptionPane.showMessageDialog(null,"Imagen eliminada.");
    }
    else {
      JOptionPane.showMessageDialog(null,"No se encontro la imagen con dicha llave.");
    }

  }

  // /////////////////////////////////////////////////////////

  // public static void main(String[] args) {
  //   ImageFinder ih = new ImageFinder();
  //
  //
  //   ih.addImage(new File("img5.jpeg"));
  //   ih.addImage(new File("img6.jpeg"));
  //
  //   System.out.println(ih.searchImage(new File("img1.jpeg")));
  //
  //   ih.addImage(new File("img1.jpeg"));
  //   ih.deleteImage(new File("img1.jpeg"));
  //   System.out.println(ih.searchImage(new File("img1.jpeg")));
  //
  // }

}
