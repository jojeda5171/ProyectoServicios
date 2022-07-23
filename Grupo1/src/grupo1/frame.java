package grupo1;

import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class frame extends javax.swing.JFrame {

    DefaultTableModel model;
    Cliente cliente = new Cliente();
    JSONArray estudiantes;
    String[] titulos = {"Cedula", "Nombre", "Apellido", "Dirección", "Telefono", "Estado"};
    private String usuario;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public frame() {
        initComponents();
        cargarTabla();
        setTitulo();
        bloquearTextos();
        bloquearBotones();
    }

    public void cargarTabla() {
        try {

            model = new DefaultTableModel(null, this.titulos);
            JSONObject datos = cliente.getJSON("https://soa5swgrupo6.000webhostapp.com/api/listar.php");
            //JSONObject datos = cliente.getJSON("http://localhost/soauta/models/acceder1.php");
            //JSONObject datos = cliente.getJSON("http://localhost:8080/Grupo1_SOAWEB/webresources/generic/listar");
            this.estudiantes = (JSONArray) datos.get("estudiantes");
            for (int i = 0; i < estudiantes.length(); i++) {
                String[] estudiante = new String[6];
                JSONObject estudiantes1 = (JSONObject) this.estudiantes.get(i);
                estudiante[0] = estudiantes1.getString("CED_EST");
                estudiante[1] = estudiantes1.getString("NOM_EST");
                estudiante[2] = estudiantes1.getString("APE_EST");
                estudiante[3] = estudiantes1.getString("DIR_EST");
                estudiante[4] = estudiantes1.getString("TEL_EST");
                estudiante[5] = estudiantes1.getString("EST_EST");
                model.addRow(estudiante);
            }
            this.jtblEstudiantes.setModel(model);
        } catch (JSONException ex) {
            Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cargarTextos() {
        try {
            int indice = jtblEstudiantes.getSelectedRow();
            JSONObject estudiante = (JSONObject) estudiantes.getJSONObject(indice);
            jtxtCedula.setText(estudiante.getString("CED_EST"));
            jtxtNombre.setText(estudiante.getString("NOM_EST"));
            jtxtApellido.setText(estudiante.getString("APE_EST"));
            jtxtDireccion.setText(estudiante.getString("DIR_EST"));
            jtxtTelefono.setText(estudiante.getString("TEL_EST"));
            desbloquearTextosEditarEliminar();
            desbloquearBotonesEditarEliminar();
        } catch (JSONException ex) {
            Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void guardarEstudiante() {
        if (buscar(jtxtCedula.getText())) {
            editar();
        } else {
            try {
                RequestBody requestBody = new FormBody.Builder().add("CED_EST", jtxtCedula
                        .getText()).add("NOM_EST", jtxtNombre.getText())
                        .add("APE_EST", jtxtApellido.getText()).
                        add("TEL_EST", jtxtTelefono.getText()).
                        add("DIR_EST", jtxtDireccion.getText())
                        .build();
                JSONObject response = cliente.postJSON("https://soa5swgrupo6.000webhostapp.com/api/agregar.php", requestBody);
                boolean verificar = response.getBoolean("ok");
                if (verificar) {
                    JOptionPane.showMessageDialog(null, "Se guardo correctamente");
                    limpiarCajas();
                    bloquearTextos();
                    bloquearBotones();
                } else {
                    JOptionPane.showMessageDialog(null, "No se guardo correctamente");
                }
                cargarTabla();
            } catch (JSONException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    public void eliminarEstudiante() {
        try {
            RequestBody requestBody = new FormBody.Builder()
                    .add("CED_EST", this.jtxtCedula.getText()).build();

            JSONObject response = this.cliente.postJSON("https://soa5swgrupo6.000webhostapp.com/api/eliminar.php", requestBody);
            boolean verificar = response.getBoolean("ok");
            if (verificar) {
                JOptionPane.showMessageDialog(null, "¡Eliminacion Exitoso!");
                limpiarCajas();
            } else {
                JOptionPane.showMessageDialog(null, "Eliminacion Fallida :(");
            }
            this.cargarTabla();
            bloquearBotones();
            bloquearTextos();
            limpiarCajas();
        } catch (JSONException ex) {
            Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarEstudiante() {
        try {
            RequestBody requestBody = new FormBody.Builder().add("CED_EST", jtxtCedula
                    .getText()).add("NOM_EST", jtxtNombre.getText())
                    .add("APE_EST", jtxtApellido.getText()).
                    add("TEL_EST", jtxtTelefono.getText()).
                    add("DIR_EST", jtxtDireccion.getText())
                    .build();
            JSONObject response = cliente.postJSON("https://soa5swgrupo6.000webhostapp.com/api/editar.php", requestBody);
            boolean verificar = response.getBoolean("ok");
            if (verificar) {
                JOptionPane.showMessageDialog(null, "Se edito correctamente");
                limpiarCajas();
            } else {
                JOptionPane.showMessageDialog(null, "No se edito correctamente");
            }
            cargarTabla();
            limpiarCajas();
            bloquearBotones();
            bloquearTextos();
        } catch (JSONException ex) {
            System.out.println("prueba error");
        }
    }

    public boolean buscar(String cedula) {
        boolean buscar = false;
        try {
            RequestBody requestBody = new FormBody.Builder().add("CED_EST", cedula).build();
            JSONObject response = cliente.postJSON("https://soa5swgrupo6.000webhostapp.com/api/buscar-id.php", requestBody);
            if (response != null) {
                buscar = true;
            } else {
                buscar = false;
            }
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return buscar;
    }

    public void editar() {
        try {
            RequestBody requestBody = new FormBody.Builder().add("CED_EST", jtxtCedula
                    .getText()).build();
            JSONObject response = cliente.postJSON("https://soa5swgrupo6.000webhostapp.com/api/editar-estado.php", requestBody);
            boolean verificar = response.getBoolean("ok");
            if (verificar) {
                JOptionPane.showMessageDialog(null, "Se coloco al estudiante: " + jtxtCedula.getText() + " como activo");
                limpiarCajas();
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo activarle al estudiante");
            }
            cargarTabla();
            limpiarCajas();
            bloquearTextos();
            bloquearBotones();
        } catch (JSONException ex) {
            System.out.println(ex);
        }
    }

    public void limpiarCajas() {
        this.jtxtCedula.setText("");
        this.jtxtNombre.setText("");
        this.jtxtApellido.setText("");
        this.jtxtTelefono.setText("");
        this.jtxtDireccion.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jtxtCedula = new javax.swing.JTextField();
        jtxtNombre = new javax.swing.JTextField();
        jtxtTelefono = new javax.swing.JTextField();
        jtxtDireccion = new javax.swing.JTextField();
        jtxtApellido = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jbtnGuardar = new javax.swing.JButton();
        jbtnEditar = new javax.swing.JButton();
        jbtnEliminar = new javax.swing.JButton();
        jbtnCancelar = new javax.swing.JButton();
        jbtnNuevo = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jtxtBuscar = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblEstudiantes = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jbtnCambiarPass = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jlblBienvenido = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(getIconImage());
        setIconImages(getIconImages());
        setLocation(new java.awt.Point(500, 270));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(800, 500));
        jPanel1.setMinimumSize(new java.awt.Dimension(800, 500));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 500));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("CÉDULA");

        jLabel2.setText("NOMBRE");

        jLabel3.setText("APELLIDO");

        jLabel4.setText("TELÉFONO");

        jLabel5.setText("DIRECCIÓN");

        jtxtCedula.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jtxtCedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtCedulaActionPerformed(evt);
            }
        });

        jtxtNombre.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jtxtTelefono.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jtxtDireccion.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jtxtApellido.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtxtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jtxtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jtxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtxtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jtxtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtxtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 570, 150));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jbtnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar.png"))); // NOI18N
        jbtnGuardar.setText("Guardar");
        jbtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnGuardarActionPerformed(evt);
            }
        });

        jbtnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editar.png"))); // NOI18N
        jbtnEditar.setText("Editar");
        jbtnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEditarActionPerformed(evt);
            }
        });

        jbtnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eliminar.png"))); // NOI18N
        jbtnEliminar.setText("Eliminar");
        jbtnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEliminarActionPerformed(evt);
            }
        });

        jbtnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar.png"))); // NOI18N
        jbtnCancelar.setText("Cancelar");
        jbtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCancelarActionPerformed(evt);
            }
        });

        jbtnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo.png"))); // NOI18N
        jbtnNuevo.setText("Nuevo");
        jbtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNuevoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbtnNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtnNuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnGuardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnEditar)
                .addGap(6, 6, 6)
                .addComponent(jbtnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnCancelar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 50, 190, 190));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jtxtBuscar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jtxtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtBuscarActionPerformed(evt);
            }
        });
        jtxtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtxtBuscarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtxtBuscarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jtxtBuscarKeyTyped(evt);
            }
        });

        jLabel6.setText("BUSCAR");

        jtblEstudiantes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jtblEstudiantes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtblEstudiantesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jtblEstudiantes);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtxtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 108, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 244, 780, 250));

        jPanel5.setBackground(new java.awt.Color(0, 134, 190));

        jbtnCambiarPass.setText("Cambiar Contraseña");
        jbtnCambiarPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCambiarPassActionPerformed(evt);
            }
        });

        jButton1.setText("Cerrar Sesión");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jlblBienvenido.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jlblBienvenido.setForeground(new java.awt.Color(255, 255, 255));
        jlblBienvenido.setText("BIENVENIDO");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jlblBienvenido)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 420, Short.MAX_VALUE)
                .addComponent(jbtnCambiarPass)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnCambiarPass)
                    .addComponent(jButton1)
                    .addComponent(jlblBienvenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jtxtCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtCedulaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtCedulaActionPerformed

    private void jtblEstudiantesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtblEstudiantesMouseClicked
        cargarTextos();
        jtxtCedula.setEnabled(false);
    }//GEN-LAST:event_jtblEstudiantesMouseClicked

    private void jbtnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEditarActionPerformed
        editarEstudiante();
    }//GEN-LAST:event_jbtnEditarActionPerformed

    private void jbtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnGuardarActionPerformed
        guardarEstudiante();
    }//GEN-LAST:event_jbtnGuardarActionPerformed

    private void jbtnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEliminarActionPerformed
        eliminarEstudiante();
    }//GEN-LAST:event_jbtnEliminarActionPerformed

    private void jbtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNuevoActionPerformed
        desbloquearTextos();
        desbloquearBotones();
    }//GEN-LAST:event_jbtnNuevoActionPerformed

    private void jtxtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtBuscarActionPerformed

    }//GEN-LAST:event_jtxtBuscarActionPerformed

    private void jtxtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtBuscarKeyReleased
        buscar();
    }//GEN-LAST:event_jtxtBuscarKeyReleased

    private void jtxtBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtBuscarKeyTyped

    }//GEN-LAST:event_jtxtBuscarKeyTyped

    private void jtxtBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtxtBuscarKeyPressed

    }//GEN-LAST:event_jtxtBuscarKeyPressed

    private void jbtnCambiarPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCambiarPassActionPerformed
        cambiarPass();
    }//GEN-LAST:event_jbtnCambiarPassActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        cerrarSesion();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jbtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCancelarActionPerformed
        bloquearBotones();
        bloquearTextos();
        limpiarCajas();
    }//GEN-LAST:event_jbtnCancelarActionPerformed

    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnCambiarPass;
    private javax.swing.JButton jbtnCancelar;
    private javax.swing.JButton jbtnEditar;
    private javax.swing.JButton jbtnEliminar;
    private javax.swing.JButton jbtnGuardar;
    private javax.swing.JButton jbtnNuevo;
    private javax.swing.JLabel jlblBienvenido;
    private javax.swing.JTable jtblEstudiantes;
    private javax.swing.JTextField jtxtApellido;
    private javax.swing.JTextField jtxtBuscar;
    private javax.swing.JTextField jtxtCedula;
    private javax.swing.JTextField jtxtDireccion;
    private javax.swing.JTextField jtxtNombre;
    private javax.swing.JTextField jtxtTelefono;
    // End of variables declaration//GEN-END:variables

    private void buscar() {
        try {
            model = new DefaultTableModel(null, this.titulos);
            RequestBody requestBody = new FormBody.Builder().add("CED_EST", this.jtxtBuscar.getText()).build();
            JSONObject datos = cliente.postJSON("https://soa5swgrupo6.000webhostapp.com/api/buscar.php?CED_EST=" + this.jtxtBuscar.getText(), requestBody);
            if (datos == null) {
                JOptionPane.showMessageDialog(null, "No existen coincidencias");
                this.jtxtBuscar.setText("");
                cargarTabla();
                this.jtxtCedula.requestFocus();
            } else {
                this.estudiantes = (JSONArray) datos.get("estudiantes");
                for (int i = 0; i < estudiantes.length(); i++) {
                    String[] estudiante = new String[6];
                    JSONObject estudiantes1 = (JSONObject) this.estudiantes.get(i);
                    estudiante[0] = estudiantes1.getString("CED_EST");
                    estudiante[1] = estudiantes1.getString("NOM_EST");
                    estudiante[2] = estudiantes1.getString("APE_EST");
                    estudiante[3] = estudiantes1.getString("DIR_EST");
                    estudiante[4] = estudiantes1.getString("TEL_EST");
                    estudiante[5] = estudiantes1.getString("EST_EST");
                    model.addRow(estudiante);
                }
            }
            this.jtblEstudiantes.setModel(model);
        } catch (JSONException ex) {
            JOptionPane.showMessageDialog(null, "Error");
        }
    }

    private void cambiarPass() {
        String nuevaPass = JOptionPane.showInputDialog("Ingrese su nueva contraseña");
        String confirmarPass = JOptionPane.showInputDialog("Ingrese su nueva contraseña");
        if (pass(nuevaPass, confirmarPass)) {
            try {
                RequestBody requestBody = new FormBody.Builder().add("USER", this.usuario)
                        .add("PASS", nuevaPass)
                        .build();
                JSONObject response = cliente.postJSON("https://soa5swgrupo6.000webhostapp.com/users/cambiar-pass.php", requestBody);
                boolean verificar = response.getBoolean("ok");
                if (verificar) {
                    JOptionPane.showMessageDialog(null, "¡Su contraseña se cambio con exito!");
                    limpiarCajas();
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo cambiar la contraseña, intente mas tarde.");
                }
                LogIn l = new LogIn();
                l.setVisible(true);
                this.dispose();
            } catch (JSONException ex) {
                System.out.println(ex);
            }
        }
    }

    private boolean pass(String pass, String confirmPass) {
        if (pass.equals(confirmPass)) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.");
            return false;
        }
    }

    public void cerrarSesion() {
        int resp = JOptionPane.showConfirmDialog(null, "¿Seguro que desea cerra sesión?",//<- EL MENSAJE
                "¡Alerta!"/*<- El título de la ventana*/, JOptionPane.YES_NO_OPTION/*Las opciones (si o no)*/, JOptionPane.WARNING_MESSAGE/*El tipo de ventana, en este caso WARNING*/);
        //Si la respuesta es sí(YES_OPTION)   
        if (resp == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "Sesión cerrada con exito.");
            LogIn l = new LogIn();
            l.setVisible(true);
            this.dispose();
        }
    }

    private void setTitulo() {
        this.setTitle("Principal");
    }

    @Override
    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().
                getImage(ClassLoader.getSystemResource("imagenes/frame.png"));

        return retValue;
    }

    public void desbloquearTextos() {
        jtxtCedula.setEnabled(true);
        jtxtNombre.setEnabled(true);
        jtxtApellido.setEnabled(true);
        jtxtDireccion.setEnabled(true);
        jtxtTelefono.setEnabled(true);
    }

    public void desbloquearBotones() {
        jbtnNuevo.setEnabled(false);
        jbtnGuardar.setEnabled(true);
        jbtnEditar.setEnabled(false);
        jbtnEliminar.setEnabled(false);
        jbtnCancelar.setEnabled(true);
    }

    public void bloquearBotones() {
        jbtnNuevo.setEnabled(true);
        jbtnGuardar.setEnabled(false);
        jbtnEditar.setEnabled(false);
        jbtnEliminar.setEnabled(false);
        jbtnCancelar.setEnabled(false);
    }

    public void bloquearTextos() {
        jtxtCedula.setEnabled(false);
        jtxtNombre.setEnabled(false);
        jtxtApellido.setEnabled(false);
        jtxtDireccion.setEnabled(false);
        jtxtTelefono.setEnabled(false);
    }

    public void desbloquearBotonesEditarEliminar() {
        jbtnNuevo.setEnabled(false);
        jbtnGuardar.setEnabled(false);
        jbtnEditar.setEnabled(true);
        jbtnEliminar.setEnabled(true);
        jbtnCancelar.setEnabled(true);
    }

    public void desbloquearTextosEditarEliminar() {
        jtxtCedula.setEnabled(false);
        jtxtNombre.setEnabled(true);
        jtxtApellido.setEnabled(true);
        jtxtDireccion.setEnabled(true);
        jtxtTelefono.setEnabled(true);
    }
}
