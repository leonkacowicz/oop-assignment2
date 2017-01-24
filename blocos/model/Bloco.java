package blocos.model;
import java.awt.Graphics;
import java.util.Map;

public interface Bloco
{
	public void ConectaBloco(Direcao d, Bloco b);
	public void Mostra(Graphics g);
    public void Atualizar();
	public void RemoveBloco(Direcao d);
	public Map<String, String> ObterAtributos();
    public void CarregarAtributos(Map<String, String> mapAtributos) throws ExcessaoAtributosInvalidos;

    public void DefinirTamanho(int TamanhoEmPixels);
    public void EventoClique(int x, int y);
    public void EventoMovimentoMouse(int x, int y);
    public void EventoRodaMouse(int x, int y, int valor);
    public void EventoTecla(String NomeTecla);
    public void EventoTimer();
    
}