package grupo1;

import java.awt.HeadlessException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
                JOptionPane.showMessageDialog(null, "Se coloco al estudiante: " + this.jtxtCedula + " como activo");
                limpiarCajas();
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo activarle al estudiante");
            }
            cargarTabla();
        } catch (JSONException ex) {
            System.out.println(ex);
        }
    }

    public void limpiarCajas() {
        this.jtxtCedula.setText("");
        this.jtxtCedula.enable();
        this.jtxtNombre.setText("");
        this.jtxtApellido.setText("");
        this.jtxtTelefono.setText("");
        this.jtxtDireccion.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jtblEstudiantes = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jtxtCedula = new javax.swing.JTextField();
        jtxtNombre = new javax.swing.JTextField();
        jtxtApellido = new javax.swing.JTextField();
        jtxtTelefono = new javax.swing.JTextField();
        jtxtDireccion = new javax.swing.JTextField();
        jbtnNuevo = new javax.swing.JButton();
        jbtnGuardar = new javax.swing.JButton();
        jbtnEditar = new javax.swing.JButton();
        jbtnEliminar = new javax.swing.JButton();
        jbtnCancelar = new javax.swing.JButton();
        jtxtBuscar = new javax.swing.JTextField();
        jbtnCambiarPass = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        jLabel1.setText("Cedula:");

        jLabel2.setText("Nombre:");

        jLabel3.setText("Apellido:");

        jLabel4.setText("Telefono:");

        jLabel5.setText("Direccion:");

        jtxtCedula.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtCedulaActionPerformed(evt);
            }
        });

        jbtnNuevo.setText("Nuevo");
        jbtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNuevoActionPerformed(evt);
            }
        });

        jbtnGuardar.setText("Guardar");
        jbtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnGuardarActionPerformed(evt);
            }
        });

        jbtnEditar.setText("Editar");
        jbtnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEditarActionPerformed(evt);
            }
        });

        jbtnEliminar.setText("Eliminar");
        jbtnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEliminarActionPerformed(evt);
            }
        });

        jbtnCancelar.setText("Cancelar");

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

        jbtnCambiarPass.setText("Cambiar Contraseña");
        jbtnCambiarPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCambiarPassActionPerformed(evt);
            }
        });

        jLabel6.setText("Buscar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jtxtCedula, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(jtxtDireccion)
                    .addComponent(jtxtTelefono)
                    .addComponent(jtxtApellido)
                    .addComponent(jtxtNombre))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jbtnNuevo)
                            .addGap(164, 164, 164))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jbtnGuardar)
                                .addComponent(jbtnEditar)
                                .addComponent(jbtnEliminar))
                            .addContainerGap()))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbtnCambiarPass)
                            .addComponent(jbtnCancelar))
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(jLabel6)
                        .addGap(37, 37, 37)
                        .addComponent(jtxtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 74, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtxtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnNuevo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jtxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnGuardar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jtxtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jbtnEditar)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jtxtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jtxtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jbtnEliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnCancelar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbtnCambiarPass)
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtxtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83))
        );

        pack();
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
        limpiarCajas();
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnCambiarPass;
    private javax.swing.JButton jbtnCancelar;
    private javax.swing.JButton jbtnEditar;
    private javax.swing.JButton jbtnEliminar;
    private javax.swing.JButton jbtnGuardar;
    private javax.swing.JButton jbtnNuevo;
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
}
