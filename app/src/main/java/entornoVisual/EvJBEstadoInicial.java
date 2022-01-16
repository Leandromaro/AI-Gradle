/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entornoVisual;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.JButton;
import logica.Configuraciones;

/**
 *
 * @author Maty
 */
public class EvJBEstadoInicial {
   private ColoresyFormas cf = new ColoresyFormas();
    
    private MouseAdapter mWheelMoved;
    private MouseAdapter mClicked;
    
    private JButton jbEstado;   

    public JButton getjbEstado(){
        return this.jbEstado;
    }
    public MouseAdapter getmWheelMoved(){
        return this.mWheelMoved;
    }
    public MouseAdapter getmClicked(){
        return this.mClicked;
    }
    
    public void setjbEstado(MouseWheelEvent e){
        
        JButton s = (JButton)e.getComponent();
        if(!MainWindow.estadoInicial){
                
            // Resguardo el estado anterior
            MainWindow.jbAnterior.setBackground(s.getBackground());
            MainWindow.jbAnterior.setText(s.getText());
            //Actualizo el Estado Inicial
            s.setBackground(Color.orange);
            s.setText("I");
            
            MainWindow.estadoInicial = true;
            MainWindow.jlEstadoInicial.setVisible(false);
            MainWindow.jBAvanza.setEnabled(true);
        }else{
                //Actualizo el Estado con los valores anteriores a que sea 
                // seleccionado como estado Inicial
                s.setBackground(MainWindow.jbAnterior.getBackground());
                s.setText(MainWindow.jbAnterior.getText());
                
                MainWindow.estadoInicial = false;
                MainWindow.jlEstadoInicial.setVisible(true);
                MainWindow.jBAvanza.setEnabled(false);
        }
        this.jbEstado = s;
        this.jbEstado.repaint();
    }
    
    public void setjbEstado(MouseEvent e){
        
        JButton s = (JButton)e.getComponent();
        
        if(s.getBackground() == Color.BLACK){
            
        }else{
            if(!MainWindow.estadoInicial){
            
                MainWindow.jbAnterior.setBackground(s.getBackground()); // Guardo lo que tenia el estado INicial antes
                MainWindow.jbAnterior.setText(s.getText());
                if(Configuraciones.getDimension() < 8){
                  s.setBackground(Color.orange);
                  s.setText("INICIAL");                    
                }else{
                  s.setBackground(Color.orange);
                  s.setText("I");
                }

                MainWindow.estadoInicial = true;
                MainWindow.jlEstadoInicial.setVisible(false);
                MainWindow.jBAvanza.setVisible(true);
                MainWindow.jBAvanza.setEnabled(true);
            }else{
//                VentanaPrincipal.jlEstadoInicial.setVisible(true);
            }

            this.jbEstado = s;
            this.jbEstado.repaint();
        }
        
    };
}
 
