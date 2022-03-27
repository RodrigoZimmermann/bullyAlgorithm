/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algortimodebully;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Rodrigo Luís Zimmermann
 */
public class StartThreads {

    private static boolean isInElection = false;
    private ArrayList<Processo> processList = new ArrayList<>();
    private final NewProcess newProcess = new NewProcess();
    private final Thread newProcessThread = new Thread(newProcess);
    private final MakeRequisition makeRequisition = new MakeRequisition();
    private final Thread makeRequisitionThread = new Thread(makeRequisition);
    private final InactivateCoordinator inactivateCoordinator = new InactivateCoordinator();
    private final Thread inactivateCoordinatorThread = new Thread(inactivateCoordinator);
    private final DisableRandomProcess disableRandomProcess = new DisableRandomProcess();
    private final Thread disableRandomProcessThread = new Thread(disableRandomProcess);

    public StartThreads() {
        newProcessThread.start();
        makeRequisitionThread.start();
        //inactivateCoordinatorThread.start();
        //disableRandomProcessThread.start();
    }

    public class NewProcess implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(30000);
                    int id = processList.size();
                    Processo p = new Processo();
                    if (id == 0) {
                        p.setCoordinator(true);
                    } else {
                        p.setCoordinator(false);
                    }
                    p.setId(id);
                    p.setActive(true);
                    processList.add(p);
                    System.out.println("Criado novo processo ID: " + p.getId());
                }
            } catch (InterruptedException ex) {
                System.out.println("Exception: " + ex);
            }
        }
    }

    public class MakeRequisition implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(25000);
                    if (!processList.isEmpty()) {
                        Processo coordinator = processList.get(0);
                        Random r = new Random();
                        Processo p = processList.get(r.nextInt(processList.size()));
                        System.out.println("Requisição realizada: " + p.getId());
                        for (int i = 0; i < processList.size(); i++) {
                            if (processList.get(i).isCoordinator()) {
                                coordinator = processList.get(i);
                            }
                        }
                        if (!coordinator.isActive() && !isInElection) {
                            isInElection = true;
                            System.out.println("Coordenador inativo. Inciando novo processo de eleição: " + p.getId());
                            holdElection();
                        }
                    }
                }
            } catch (InterruptedException ex) {
                System.out.println("Exception: " + ex);
            }
        }
    }

    private Processo makeRequisition() {

        while (p.isCoordinator()) {
            System.out.println("É o coordenador: " + p.getId());
            makeRequisition();
        }

        return p;
    }

    private void holdElection() throws InterruptedException {
        for (int i = processList.size() - 1; i >= 0; i--) {
            if (!processList.get(i).isCoordinator()) {
                processList.get(i).setCoordinator(true);
                isInElection = false;
                break;
            }
        }
    }

    public class InactivateCoordinator implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(100000);
                    for (int i = 0; i < processList.size(); i++) {
                        if (processList.get(i).isCoordinator()) {
                            processList.get(i).setCoordinator(false);
                        }
                    }
                }
            } catch (InterruptedException ex) {
                System.out.println("Exception: " + ex);
            }
        }
    }

    public class DisableRandomProcess implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(80000);
                    Processo p = disableRandomProcess();
                    if (!p.isActive()) {
                        System.out.println("Já está inativo.ID: " + p.getId() + ".Buscando um novo processo para inativar");
                    } else {
                        p.setActive(false);
                        System.out.println("Inativo processo " + p.getId());
                    }

                }
            } catch (InterruptedException ex) {
                System.out.println("Exception: " + ex);
            }
        }
    }

    private Processo disableRandomProcess() {
        Random r = new Random(processList.size());
        Processo p = processList.get(Integer.parseInt(r.toString()));
        while (!p.isActive()) {
            System.out.println("Já está inativo.ID: " + p.getId() + ".Buscando um novo processo para inativar");
            disableRandomProcess();
        }
        return p;
    }
}
