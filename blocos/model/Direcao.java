package blocos.model;

/**
 *
 * @author Leon
 */
public enum Direcao {
    CIMA { public Direcao DirecaoOposta() { return BAIXO; } },
    DIREITA { public Direcao DirecaoOposta() { return ESQUERDA; } },
    BAIXO { public Direcao DirecaoOposta() { return CIMA; } },
    ESQUERDA { public Direcao DirecaoOposta() { return DIREITA; } };

    public abstract Direcao DirecaoOposta();
}
