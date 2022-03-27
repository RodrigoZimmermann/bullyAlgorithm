/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algortimodebully;

import static algortimodebully.AlgortimoDeBully.processList;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Rodrigo Luís Zimmermann
 */
public class Logica extends Thread {

    public ArrayList<Processo> processList = new ArrayList<>();
    private Thread newProcess = new Thread();
    private Thread makeRequisition = new Thread();
    private Thread inactivateCoordinator = new Thread();
    private Thread disableRandomProcess = new Thread();
    private boolean isInElection = false;
    private Thread election;
    public ArrayList<Processo> electionList;

    public Logica() {
        try {
            newProcess();
            makeRequisition();
            inactivateCoordinator();
            disableRandomProcess();
        } catch (InterruptedException ex) {
            System.out.println("Exception:" + ex);
        }
    }

    private  void newProcess() throws InterruptedException {
        newProcess.start();
        while (true) {
            newProcess.sleep(30000);
            int id = processList.size();
            Processo p = new Processo();
            p.setId(id);
            p.setActive(true);
            p.setCoordinator(false);
            processList.add(p);
            System.out.println("Criado novo processo ID: " + p.getId());
        }
    }

    private  void makeRequisition() throws InterruptedException {
        makeRequisition.start();
        Random r = new Random(processList.size());
        while (true) {
            Thread.sleep(25000);
            Processo p = processList.get(Integer.parseInt(r.toString()));
            boolean activeCoordinator = p.isCoordinator();
            System.out.println("Requisição realizada" + p.getId());

            if (!activeCoordinator && !isInElection) {
                System.out.println("Coordenador não respondeu. Inciando novo processo de eleição");
                election = new Thread();
                holdElection(election, p);
            }
        }
    }

    private  void holdElection(Thread election, Processo p) throws InterruptedException {
        long bigId = 0;
        int aux = 0;
        election.start();
        isInElection = true;
        System.out.println("Eleição iniciada pelo processo " + p.getId());
        for (int i = 0; i < processList.size(); i++) {
            if (!processList.get(i).isCoordinator()) {
                if (processList.get(i).getId() > bigId) {
                    bigId = processList.get(i).getId();
                    aux = i;
                }
            }
        }
        processList.get(aux).setCoordinator(true);
    }

    private  void inactivateCoordinator() throws InterruptedException {
        inactivateCoordinator.start();
        Thread.sleep(100000);
        for (int i = 0; i < processList.size(); i++) {
            if (processList.get(i).isCoordinator()) {
                processList.get(i).setCoordinator(false);
            }
        }
    }

    private  void disableRandomProcess() throws InterruptedException {
        disableRandomProcess.start();
        Random r = new Random(processList.size());
        while (true) {
            Thread.sleep(80000);
            Processo p = processList.get(Integer.parseInt(r.toString()));
            if (!p.isActive()) {
                disableRandomProcess();
                System.out.println("Já está inativo.ID: " + p.getId() + ".Buscando um novo processo para inativar");
            } else {
                p.setActive(false);
                System.out.println("Inativo processo " + p.getId());
            }

        }
    }

}
