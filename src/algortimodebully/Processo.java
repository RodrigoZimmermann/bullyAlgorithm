/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algortimodebully;

/**
 *
 * @author Rodrigo Luís Zimmermann e Matheus Felipe da Silva Sychocki
 */
public class Processo {

    private int id;
    private boolean coordinator;
    private boolean active;

    public Processo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCoordinator() {
        return coordinator;
    }

    public void setCoordinator(boolean coordinator) {
        this.coordinator = coordinator;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
