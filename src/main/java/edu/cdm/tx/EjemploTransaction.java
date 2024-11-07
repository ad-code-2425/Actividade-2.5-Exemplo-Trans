package edu.cdm.tx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EjemploTransaction {
  private  Connection  con=null;

  public EjemploTransaction() throws SQLException{
    this.con =  DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbctutorial?user=root&password=abc123.",
    "root", "abc123.");
  }
    public static void main(String[] args) {
      try {   
      EjemploTransaction ejemploTransaction = new EjemploTransaction();
         HashMap<String, Integer> ventasSemanales = new HashMap<>();
         ventasSemanales.put("Colombian", 200);
         ventasSemanales.put("French_Roast", 20);
        
          ejemploTransaction.updateCoffeeSales(ventasSemanales);
        } catch (SQLException e) {
       
          e.printStackTrace();
        }
           
    }


    public  void updateCoffeeSales(HashMap<String, Integer> salesForWeek) throws SQLException {
    String updateString =
      "update COFFEES set SALES = ? where COF_NAME = ?";
    String updateStatement =
      "update COFFEES set TOTAL = TOTAL + ? where COF_NAME = ?";

    try (PreparedStatement updateSales = con.prepareStatement(updateString);
         PreparedStatement updateTotal = con.prepareStatement(updateStatement))
    
    {
      con.setAutoCommit(false);
      for (Map.Entry<String, Integer> e : salesForWeek.entrySet()) {
        updateSales.setInt(1, e.getValue().intValue());
        updateSales.setString(2, e.getKey());
        updateSales.executeUpdate();

        updateTotal.setInt(1, e.getValue().intValue());
        updateTotal.setString(2, e.getKey());
        updateTotal.executeUpdate();
        con.commit();

        System.out.println("A transacción executouse con éxito. Comprobe os resultados na base de datos");
      }
    } catch (SQLException e) {
      JDBCTutorialUtilities.printSQLException(e);
      if (con != null) {
        try {
          System.err.print("Transaction is being rolled back");
          con.rollback();
        } catch (SQLException excep) {
          JDBCTutorialUtilities.printSQLException(excep);
        }
      }
    }
  }
}