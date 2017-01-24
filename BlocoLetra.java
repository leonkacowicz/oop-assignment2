import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import blocos.model.Bloco;
import blocos.model.Direcao;
import java.util.Random;

public class BlocoLetra implements Bloco
{
	private BlocoLetra [] BlocoVizinho = new BlocoLetra[Direcao.values().length];
	private String Letra;
	private boolean Estado = false;
    private long ult_valor_timer = System.currentTimeMillis();
    private double probVogal = 0.33;
    private int Tamanho;
    public void DefinirTamanho(int TamanhoEmPixels)
    {
        Tamanho = TamanhoEmPixels;
    }
	private String PalavraNaDirecao(Direcao d)
	{
        BlocoLetra b = BlocoVizinho[d.ordinal()];
        if (b instanceof BlocoLetra)
        {
            if ((d == Direcao.CIMA) || (d == Direcao.ESQUERDA))
                return b.PalavraNaDirecao(d) + (this.Letra);
            else
                return this.Letra + (b.PalavraNaDirecao(d));
        } else {
            return this.Letra;
        }
	}

    private String PalavraNaVertical()
    {
        String palavra = "";
        palavra += BlocoVizinho[Direcao.CIMA.ordinal()] instanceof BlocoLetra ?
                    BlocoVizinho[Direcao.CIMA.ordinal()].PalavraNaDirecao(Direcao.CIMA) : "";
        palavra += this.Letra;
        palavra += BlocoVizinho[Direcao.BAIXO.ordinal()] instanceof BlocoLetra ?
                    BlocoVizinho[Direcao.BAIXO.ordinal()].PalavraNaDirecao(Direcao.BAIXO) : "";

        return palavra;
    }
    private String PalavraNaHorizontal()
    {
        String palavra = "";
        palavra += BlocoVizinho[Direcao.ESQUERDA.ordinal()] instanceof BlocoLetra ?
                    BlocoVizinho[Direcao.ESQUERDA.ordinal()].PalavraNaDirecao(Direcao.ESQUERDA) : "";
        palavra += this.Letra;
        palavra += BlocoVizinho[Direcao.DIREITA.ordinal()] instanceof BlocoLetra ?
                    BlocoVizinho[Direcao.DIREITA.ordinal()].PalavraNaDirecao(Direcao.DIREITA) : "";

        return palavra;
    }

    public BlocoLetra()
    {
        SorteiaLetra();
    }
    private void SorteiaLetra()
    {
        Random r = new Random();
        String vogais = "aeiou";
        String consoantes = "bcdfghjklmnpqrstvwxyz";
        int pos = 0;

        if (r.nextDouble() <= probVogal)
        {
            pos  = r.nextInt(vogais.length());
            Letra = vogais.substring(pos, pos + 1);
        } else {
            pos  = r.nextInt(consoantes.length());
            Letra = consoantes.substring(pos, pos + 1);
        }
    }
	public BlocoLetra(String Letra)
	{		
		this.Letra = Letra;
	}

	public void ConectaBloco(Direcao d, Bloco b)
	{
		if (b instanceof BlocoLetra)
		{
			BlocoVizinho[d.ordinal()] = (BlocoLetra)b;
		}
	}
	
	public void Atualizar(Direcao origem)
	{
        BlocoLetra b;
        Dicionario dic = Dicionario.getDicionario();

		Estado = dic.ExistePalavra(PalavraNaVertical()) || dic.ExistePalavra(PalavraNaHorizontal());

        if (origem instanceof Direcao)
        {
            b = BlocoVizinho[origem.DirecaoOposta().ordinal()];
            if (b instanceof BlocoLetra)
                b.Atualizar(origem);
        }
        else
            for (Direcao d : Direcao.values())
                if (BlocoVizinho[d.ordinal()] instanceof BlocoLetra)
                    BlocoVizinho[d.ordinal()].Atualizar(d.DirecaoOposta());

        if (!Estado) ult_valor_timer = System.currentTimeMillis();

	}
	
	public void Mostra(Graphics g)
	{
        Letra = Letra.toUpperCase();
		if (Estado) g.setColor(java.awt.Color.BLUE);
		else g.setColor(java.awt.Color.BLACK);

        for (int tam_fonte = 40; tam_fonte >= 6; tam_fonte--) {
            g.setFont(new java.awt.Font("Tahoma", 0, tam_fonte));
            if (g.getFontMetrics().stringWidth(Letra) <= (Tamanho * 0.95) &&
                    g.getFontMetrics().getHeight() <= (Tamanho * 0.95)) break;
        }
        g.drawRect(0, 0, g.getClipBounds().width - 1, g.getClipBounds().height - 1);
        g.drawString(Letra, (g.getClipBounds().width - g.getFontMetrics().stringWidth(Letra)) / 2, (g.getClipBounds().height + g.getFontMetrics().getHeight() / 2) / 2);
	}
		
	public void RemoveBloco(Direcao d)
	{
		BlocoVizinho[d.ordinal()] = null;
	}

    public void Atualizar()
    {
        Atualizar(null);
    }

    public Map<String, String> ObterAtributos()
    {
        Map<String, String> r;
        r = new HashMap<String, String>();
        r.put("Letra", Letra);
        return r;
    }

    public void CarregarAtributos(Map<String, String> mapAtributos)
    {
        if (mapAtributos.containsKey("Letra"))
            Letra = mapAtributos.get("Letra").substring(0, 1);
    }

    public void EventoClique(int x, int y)
    {
    }

    public void EventoRodaMouse(int x, int y, int valor)
    {
        SorteiaLetra();
    }

    public void EventoTecla(String NomeTecla) { }

    public void EventoTimer()
    {
        //System.out.printf("%d %d\n", (Estado ? 1 : 0), System.currentTimeMillis() - ult_valor_timer);
        if (Estado && (System.currentTimeMillis() - ult_valor_timer > 1000))
        {
            //System.out.println("mudou");
            SorteiaLetra();
            ult_valor_timer = System.currentTimeMillis();
            
        }
    }

    public void EventoMovimentoMouse(int x, int y) { }

    
}