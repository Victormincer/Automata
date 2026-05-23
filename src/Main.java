import view.MainFrame;

import javax.swing.*;

/*---------------------------------------------------------------------------
 PROPOSITO: Clase principal del programa, se encarga de iniciar la aplicación.
 FECHA: 2026-05-23
 AUTOR: Victor Alfonso Pardo Gutierrez - Maryury Hernandez Marin
 VERSION: 1.3
 ACTUALIZACIÓN: 2026-05-23 - Se mejoró el diseño del programa, 
               con mejores colores, fuentes y estilos de los componentes gráficos.
 -----------------------------------------------------------------------------*/
public class Main {

    public static void main(String[] args) {
        // Suavizado de fuentes
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext",                "true");
        
         SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
        
    }
}
