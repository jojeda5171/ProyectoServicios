package grupo1;

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

    public frame() {
        initComponents();
        cargarTabla();
    }

    public void cargarTabla() {
        try {
            String[] titulos = {"Cedula", "Nombre", "Apellido", "Dirección", "Telefono", "Estado"};
            model = new DefaultTableModel(null, titulos);
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
        try {
            RequestBody requestBody = new FormBody.Builder().add("CED_EST", jtxtCedula
                    .getText()).add("NOM_EST", jtxtNombre.getText())
                    .add("APE_EST", jtxtApellido.getText()).
                    
                    add("TEL_EST", jtxtTelefono.getText()).
                    add("DIR_EST", jtxtDireccion.getText())
                    .build();
            JSONObject response = cliente.postJSON("https://soa5swgrupo6.000webhostapp.com/api/agregar.php", requestBody);
            //JSONObject response = cliente.postJSON("http://localhost/soauta/models/guardar1.php", requestBody);
            //JSONObject response = cliente.postJSON("http://localhost:8080/Grupo1_SOAWEB/webresources/generic/guardar", requestBody);
            boolean verificar = response.getBoolean("ok");
            if (verificar) {
                JOptionPane.showMessageDialog(null, "Se guardo correctamente");
                limpiarCajas();
            } else {
                JOptionPane.showMessageDialog(null, "No se guardo correctamente");
            }
            cargarTabla();
        } catch (JSONException ex) {
            //Logger.getLogger(frame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void eliminarEstudiante() {
        try {
            RequestBody requestBody = new FormBody.Builder()
                    .add("CED_EST", this.jtxtCedula.getText()).build();

            JSONObject response = this.cliente.postJSON("https://soa5swgrupo6.000webhostapp.com/api/eliminar.php", requestBody);
            //JSONObject response = this.cliente.postJSON("http://localhost/soauta/models/eliminar1.php", requestBody);
            //JSONObject response = this.cliente.postJSON("http://localhost:8080/Grupo1_SOAWEB/webresources/generic/eliminar", requestBody);

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
            //JSONObject response = cliente.putJSON("http://localhost/soauta/models/editar1.php", requestBody);
            //JSONObject response = cliente.putJSON("http://localhost:8080/Grupo1_SOAWEB/webresources/generic/editar", requestBody);
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
    
    public void limpiarCajas(){
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jbtnNuevo)
                            .addGap(164, 164, 164))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jbtnGuardar)
                                .addComponent(jbtnEditar))
                            .addContainerGap()))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbtnEliminar)
                        .addGap(164, 164, 164))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jbtnCancelar)
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addGap(14, 14, 14)
                        .addComponent(jbtnEliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnCancelar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbtnCancelar;
    private javax.swing.JButton jbtnEditar;
    private javax.swing.JButton jbtnEliminar;
    private javax.swing.JButton jbtnGuardar;
    private javax.swing.JButton jbtnNuevo;
    private javax.swing.JTable jtblEstudiantes;
    private javax.swing.JTextField jtxtApellido;
    private javax.swing.JTextField jtxtCedula;
    private javax.swing.JTextField jtxtDireccion;
    private javax.swing.JTextField jtxtNombre;
    private javax.swing.JTextField jtxtTelefono;
    // End of variables declaration//GEN-END:variables
}
