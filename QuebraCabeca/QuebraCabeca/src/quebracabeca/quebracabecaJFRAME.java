package quebracabeca;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class quebracabecaJFRAME extends javax.swing.JFrame {

    ArrayList<Integer> lista = new ArrayList<>();
    JButton[] botoes;
    private int jogadas = 0;
    private JLabel labelJogadas;
    private JLabel labelTempo;
    private Timer cronometro;
    private int segundos = 0;

    public quebracabecaJFRAME() {
        initComponents();
        botoes = new JButton[]{b1, b2, b3, b4, b5, b6, b7, b8,
            b9, b10, b11, b12, b13, b14, b15, b16};
        adicionarEventos();
    }

    private void initComponents() {
        b1 = new JButton(); b2 = new JButton(); b3 = new JButton(); b4 = new JButton();
        b5 = new JButton(); b6 = new JButton(); b7 = new JButton(); b8 = new JButton();
        b9 = new JButton(); b10 = new JButton(); b11 = new JButton(); b12 = new JButton();
        b13 = new JButton(); b14 = new JButton(); b15 = new JButton(); b16 = new JButton();
        jLabel1 = new JLabel();
        labelJogadas = new JLabel("Jogadas: 0");
        labelTempo = new JLabel("Tempo: 0s");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        JButton[] botoesTemp = {b1, b2, b3, b4, b5, b6, b7, b8,
                                b9, b10, b11, b12, b13, b14, b15, b16};
        int x = 30, y = 30;
        for (int i = 0; i < botoesTemp.length; i++) {
            botoesTemp[i].setBounds(x, y, 80, 60);
            add(botoesTemp[i]);
            x += 80;
            if ((i + 1) % 4 == 0) {
                x = 30;
                y += 60;
            }
        }

        jLabel1.setText("Quebra-Cabeça 15");
        jLabel1.setBounds(120, 280, 200, 30);
        add(jLabel1);

        labelJogadas.setBounds(370, 30, 100, 20);
        add(labelJogadas);

        labelTempo.setBounds(370, 60, 100, 20);
        add(labelTempo);

        setSize(500, 400);
        setLocationRelativeTo(null); // Centraliza a janela
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                iniciarJogo();
            }
        });
    }

    private void iniciarJogo() {
        jogadas = 0;
        segundos = 0;
        labelJogadas.setText("Jogadas: 0");
        labelTempo.setText("Tempo: 0s");
        iniciarCronometro();
        embaralhar();
    }

    private void iniciarCronometro() {
        if (cronometro != null) {
            cronometro.stop();
        }
        cronometro = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                segundos++;
                labelTempo.setText("Tempo: " + segundos + "s");
            }
        });
        cronometro.start();
    }

    private void embaralhar() {
        do {
            lista.clear();
            for (int i = 1; i <= 15; i++) {
                lista.add(i);
            }
            lista.add(null);
            Collections.shuffle(lista);
        } while (!ehResolvido());
        atualizarBotoes();
    }

    private void atualizarBotoes() {
        for (int i = 0; i < 16; i++) {
            botoes[i].setText(lista.get(i) == null ? "" : lista.get(i).toString());
        }
    }

    private void adicionarEventos() {
        for (int i = 0; i < botoes.length; i++) {
            final int idx = i;
            botoes[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    mover(idx);
                }
            });
        }
    }

    private void mover(int idx) {
        int linha = idx / 4;
        int coluna = idx % 4;
        int[][] direcoes = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : direcoes) {
            int novaLinha = linha + dir[0];
            int novaColuna = coluna + dir[1];
            if (novaLinha >= 0 && novaLinha < 4 && novaColuna >= 0 && novaColuna < 4) {
                int novoIdx = novaLinha * 4 + novaColuna;
                if (lista.get(novoIdx) == null) {
                    Collections.swap(lista, idx, novoIdx);
                    jogadas++;
                    labelJogadas.setText("Jogadas: " + jogadas);
                    atualizarBotoes();
                    verificarVitoria();
                    return;
                }
            }
        }
    }

    private void verificarVitoria() {
        for (int i = 0; i < 15; i++) {
            if (lista.get(i) == null || lista.get(i) != i + 1) return;
        }
        cronometro.stop();
        int resposta = JOptionPane.showConfirmDialog(
            this,
            "Parabéns! Você venceu!\nJogadas: " + jogadas + "\nTempo: " + segundos + "s\n\nDeseja jogar novamente?",
            "Vitória!",
            JOptionPane.YES_NO_OPTION
        );
        if (resposta == JOptionPane.YES_OPTION) {
            iniciarJogo();
        } else {
            System.exit(0);
        }
    }

    private boolean ehResolvido() {
        int inversoes = 0;
        int[] valores = new int[16];
        int linhaEspaco = 0;

        for (int i = 0; i < 16; i++) {
            if (lista.get(i) == null) {
                linhaEspaco = 3 - (i / 4);
                valores[i] = 0;
            } else {
                valores[i] = lista.get(i);
            }
        }

        for (int i = 0; i < 16; i++) {
            if (valores[i] == 0) continue;
            for (int j = i + 1; j < 16; j++) {
                if (valores[j] != 0 && valores[i] > valores[j]) inversoes++;
            }
        }

        return (linhaEspaco % 2 == 0) ? (inversoes % 2 != 0) : (inversoes % 2 == 0);
    }

    public static void main(String[] args) {
        new quebracabecaJFRAME().setVisible(true);
    }

    private JButton b1, b2, b3, b4, b5, b6, b7, b8;
    private JButton b9, b10, b11, b12, b13, b14, b15, b16;
    private JLabel jLabel1;
}
