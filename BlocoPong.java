import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import blocos.model.Bloco;
import blocos.model.Direcao;
import blocos.model.ExcessaoAtributosInvalidos;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class BlocoPong implements Bloco
{
	private BlocoPong [] BlocoVizinho = new BlocoPong[Direcao.values().length];    
    private ArrayList<Bola> vetBolas;
    private long ult_valor_timer = 0;
    private int Tamanho;
    public void DefinirTamanho(int TamanhoEmPixels)
    {
        Tamanho = TamanhoEmPixels;
    }

    public BlocoPong()
    {
        Random r = new Random();
        vetBolas = new ArrayList<Bola>();
        vetBolas.add(new Bola(3, new Point(20 + r.nextInt(20), 20 + r.nextInt(20)), new Point(5,5)));
    }

	public void ConectaBloco(Direcao d, Bloco b)
	{
		if (b instanceof BlocoPong) BlocoVizinho[d.ordinal()] = (BlocoPong)b;
	}

    private void AcrescentaBola(Bola b)
    {
        vetBolas.add(b);
    }
    public void EventoTimer()
    {
        if (System.currentTimeMillis() - ult_valor_timer > 50)
        {
            ult_valor_timer = System.currentTimeMillis();
            for (int i = 0; i < vetBolas.size(); i++)
            {
                Bola b = vetBolas.get(i);
                b.Step(Tamanho, Tamanho);
                if (!b.isInvalida() && b.Posicao.x + b.raio >= Tamanho  && b.Velocidade.x > 0)
                    if (BlocoVizinho[Direcao.DIREITA.ordinal()] instanceof BlocoPong)
                    {
                        b.Posicao.x = -b.raio;
                        BlocoVizinho[Direcao.DIREITA.ordinal()].AcrescentaBola(new Bola(b));
                        b.Invalidar();
                    } else
                        b.Velocidade.x = -b.Velocidade.x;
                
                if (!b.isInvalida() && b.Posicao.y + b.raio >= Tamanho && b.Velocidade.y > 0)
                    if (BlocoVizinho[Direcao.BAIXO.ordinal()] instanceof BlocoPong)
                    {
                        b.Posicao.y = -b.raio;
                        BlocoVizinho[Direcao.BAIXO.ordinal()].AcrescentaBola(new Bola(b));
                        b.Invalidar();
                    } else
                        b.Velocidade.y = -b.Velocidade.y;

                if (!b.isInvalida() && b.Posicao.x - b.raio <= 0 && b.Velocidade.x < 0)
                    if (BlocoVizinho[Direcao.ESQUERDA.ordinal()] instanceof BlocoPong)
                    {
                        b.Posicao.x = Tamanho + b.raio;
                        BlocoVizinho[Direcao.ESQUERDA.ordinal()].AcrescentaBola(new Bola(b));
                        b.Invalidar();
                    } else
                        b.Velocidade.x = -b.Velocidade.x;

                if (!b.isInvalida() && b.Posicao.y - b.raio <= 0  && b.Velocidade.y < 0)
                    if (BlocoVizinho[Direcao.CIMA.ordinal()] instanceof BlocoPong)
                    {
                        b.Posicao.y = Tamanho + b.raio;
                        BlocoVizinho[Direcao.CIMA.ordinal()].AcrescentaBola(new Bola(b));
                        b.Invalidar();
                    } else
                        b.Velocidade.y = -b.Velocidade.y;
            }
            int i = 0;

            while (i < vetBolas.size())
            {
                if (vetBolas.get(i).isInvalida()) vetBolas.remove(i);
                else i++;
            }

            
        }
    }
	public void Mostra(Graphics g)
	{
        g.setColor(new Color(100, 255, 100));
        g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);

        g.setColor(java.awt.Color.BLUE);
        if (!(BlocoVizinho[Direcao.CIMA.ordinal()] instanceof BlocoPong))
            g.drawLine(0, 0, g.getClipBounds().width - 1, 0);
        if (!(BlocoVizinho[Direcao.BAIXO.ordinal()] instanceof BlocoPong))
            g.drawLine(0, g.getClipBounds().height - 1, g.getClipBounds().width - 1, g.getClipBounds().height - 1);
        if (!(BlocoVizinho[Direcao.ESQUERDA.ordinal()] instanceof BlocoPong))
            g.drawLine(0, 0, 0, g.getClipBounds().height - 1);
        if (!(BlocoVizinho[Direcao.DIREITA.ordinal()] instanceof BlocoPong))
            g.drawLine(g.getClipBounds().width - 1, 0, g.getClipBounds().width - 1, g.getClipBounds().height - 1);

        //g.drawRect(0, 0, g.getClipBounds().width - 1, g.getClipBounds().height - 1);
        for (int i = 0; i < vetBolas.size(); i++)
        {
            Bola b = vetBolas.get(i);
            g.setColor(b.cor);
            g.fillRoundRect(b.Posicao.x - b.raio, b.Posicao.y - b.raio, 2 * b.raio, 2 * b.raio, b.raio, b.raio);
        }
        
	}

	public void RemoveBloco(Direcao d)
	{
		BlocoVizinho[d.ordinal()] = null;
	}

    public void Atualizar()
    {
        
    }

    public Map<String, String> ObterAtributos()
    {
        Map<String, String> r;
        r = new HashMap<String, String>();

        return r;
    }

    public void CarregarAtributos(Map<String, String> mapAtributos) throws blocos.model.ExcessaoAtributosInvalidos
    {
        try
        {
            //mapAtributos.get("Letra").substring(0, 1);
        } catch (Exception e) {
            throw new ExcessaoAtributosInvalidos();
        }
    }

    public void EventoClique(int x, int y){}
    public void EventoRodaMouse(int x, int y, int valor){}
    public void EventoTecla(String NomeTecla){}
    public void EventoMovimentoMouse(int x, int y) {}


    private static class Bola
    {
        Point Posicao = new Point();
        Point Velocidade = new Point();
        int raio;
        Color cor = new Color(0, 0, 255);
        private boolean Invalida = false;
        public Bola (int raio, Point Pos, Point Vel)
        {
            Random r = new Random();
            this.raio = raio;
            Posicao.setLocation(Pos);
            Velocidade.setLocation(Vel);
            
            int c = r.nextInt(256);
            cor = new Color(c, 0, 255 - c);
        }
        public Bola (Bola b)
        {
            Posicao.x = b.Posicao.x;
            Posicao.y = b.Posicao.y;
            Velocidade.x = b.Velocidade.x;
            Velocidade.y = b.Velocidade.y;
            cor = b.cor;
            raio = b.raio;
        }
        public void Step(int LimiteVertical, int LimiteHorizontal)
        {
            Posicao.setLocation(Posicao.x + Velocidade.x, Posicao.y + Velocidade.y);
        }
        public void Invalidar()
        {
            Invalida = true;
        }
        public boolean isInvalida()
        {
            return Invalida;
        }
    }
}