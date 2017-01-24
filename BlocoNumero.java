
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Map;
import blocos.model.Bloco;
import blocos.model.Direcao;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


public class BlocoNumero implements Bloco
{
    private BlocoNumero [] BlocoVizinho = new BlocoNumero[Direcao.values().length];
	private String Simbolo;
	private int Tamanho;
    public void DefinirTamanho(int TamanhoEmPixels)
    {
        Tamanho = TamanhoEmPixels;
    }
    public BlocoNumero() { this(""); }
    
	public BlocoNumero(String Simbolo)
	{
		BlocoVizinho = new BlocoNumero[4];		
		this.Simbolo = Simbolo;
	}

	public void ConectaBloco(Direcao d, Bloco b)
	{
		if (b instanceof BlocoNumero)
		{
			BlocoVizinho[d.ordinal()] = (BlocoNumero)b;
		}
	}

    public String Expressao(Direcao d)
    {
        BlocoNumero b = BlocoVizinho[d.ordinal()];
        if (b instanceof BlocoNumero)
        {
            if (d.equals(Direcao.CIMA) || d.equals(Direcao.ESQUERDA))
            {
                return b.Expressao(d) + Simbolo;
            }
        }
        return Simbolo;
    }
    public void Atualizar()
    {
        Atualizar(null);
    }
    @SuppressWarnings("empty-statement")
    public void Atualizar(Direcao origem)
    {
        // Se Atualiza
        if (Simbolo.equals("="))
        {
            BlocoNumero b_esq = BlocoVizinho[Direcao.ESQUERDA.ordinal()];
            BlocoNumero b_dir = BlocoVizinho[Direcao.DIREITA.ordinal()];

            if (b_esq instanceof BlocoNumero && b_dir instanceof BlocoNumero)
            {
                ScriptEngine javascript = (new ScriptEngineManager()).getEngineByName("js");
                try {
                    Double r = (Double)javascript.eval(b_esq.Expressao(Direcao.ESQUERDA));
                    b_dir.SetSimbolo(String.valueOf(r.intValue()));
                } catch (ScriptException ex) {}

            }
        }

        // Propaga atualizacao para o proximo vizinho
        if (origem instanceof Direcao)
        {
            BlocoNumero b = BlocoVizinho[origem.DirecaoOposta().ordinal()];
            if (b instanceof BlocoNumero) b.Atualizar(origem);

        }
        else
            for (Direcao d : Direcao.values())
                if (BlocoVizinho[d.ordinal()] instanceof BlocoNumero)
                    BlocoVizinho[d.ordinal()].Atualizar(d.DirecaoOposta());
    }
	public void SetSimbolo(String NovoSimbolo)
    {        
        Simbolo = NovoSimbolo;
        BlocoNumero b = BlocoVizinho[Direcao.DIREITA.ordinal()];
        if (b instanceof BlocoNumero)
        {
            try
            {
                Integer.parseInt(NovoSimbolo);
                /*
                if (!b.GetSimbolo().equals(""))
                    Integer.parseInt(b.GetSimbolo());
                */
                
                b.SetSimbolo(NovoSimbolo);
                if (b.GetSimbolo().length() > 1)
                {
                    Simbolo = NovoSimbolo.substring(0, b.GetSimbolo().length() - 1);
                    b.SetSimbolo(NovoSimbolo.substring(b.GetSimbolo().length() - 1));
                } else {
                    Simbolo = "0";
                }
            }
            catch (Exception e){}
        }
    }
    public String GetSimbolo()
    {
        return Simbolo;
    }
	public void RemoveBloco(Direcao d)
	{
		BlocoVizinho[d.ordinal()] = null;
	}

    public void Mostra(Graphics g) 
    {
        int tam_fonte;
        g.setColor(java.awt.Color.BLACK);
        for (tam_fonte = 40; tam_fonte >= 6; tam_fonte--) {
            g.setFont(new java.awt.Font("Tahoma", 0, tam_fonte));
            if (g.getFontMetrics().stringWidth(Simbolo) <= (Tamanho * 0.95) &&
                    g.getFontMetrics().getHeight() <= (Tamanho * 0.95)) break;
        }
        g.drawRect(0, 0, g.getClipBounds().width - 1, g.getClipBounds().height - 1);
        
        g.drawString(Simbolo, (g.getClipBounds().width - g.getFontMetrics().stringWidth(Simbolo)) / 2, (g.getClipBounds().height + g.getFontMetrics().getHeight() / 2) / 2);
    }

    public Map<String, String> ObterAtributos() {
        Map<String, String> r;
        r = new HashMap<String, String>();
        r.put("Simbolo", Simbolo);
        return r;
    }

    public void CarregarAtributos(Map<String, String> mapAtributos) {
        if (mapAtributos.containsKey("Simbolo"))
            if (mapAtributos.get("Simbolo").length() > 0)
                Simbolo = mapAtributos.get("Simbolo").substring(0, 1);
    }

    public void EventoClique(int x, int y) {}

    public void EventoTecla(String Tecla)
    {
        if ((Tecla.charAt(0) < '0' ||
            Tecla.charAt(0) > '9') &&
            Tecla.charAt(0) != '+' &&
            Tecla.charAt(0) != '*' &&
            Tecla.charAt(0) != '-' &&
            Tecla.charAt(0) != '/' &&
            Tecla.charAt(0) != '=' &&
            Tecla.charAt(0) != '(' &&
            Tecla.charAt(0) != ')' &&

            Tecla.charAt(0) != ' ')
            return;

        Tecla = Tecla.replaceAll(" ", "");
        Simbolo = (String.format("%s", Tecla));
    }

    public void EventoTimer() {}

    public void EventoMovimentoMouse(int x, int y) {}

    public void EventoRodaMouse(int x, int y, int valor) {}

}