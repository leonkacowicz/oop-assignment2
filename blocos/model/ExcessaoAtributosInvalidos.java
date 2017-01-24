/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package blocos.model;

/**
 *
 * @author Leon
 */
public class ExcessaoAtributosInvalidos extends Exception {
    @Override
    public String toString()
    {
        return "Conjunto inválido de atributos ou atributos com valor inválido.";
    }
}
