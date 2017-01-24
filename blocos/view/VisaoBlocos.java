package blocos.view;

import java.awt.Graphics;
/**
 *
 * @author Leon
 */
public interface VisaoBlocos {

    /*
     Quem implementa uma visao de blocos deve fornecer um metodo que retorna um
     objeto graphics onde um bloco localizado na posicao (x, y) deve se desenhar
     */
    public Graphics ObterGraphics(Graphics g, int x, int y);
    public void DesenhaBlocos(Graphics g);
    public void DesenhaBlocos();
    public int ObterTamanhoBlocos();
}
