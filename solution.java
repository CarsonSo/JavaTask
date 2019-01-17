package mysolution;
import java.sql.*;

public class solution {
  public static double StandardNormalDistribution(){
    java.util.Random random = new java.util.Random();
    return random.nextGaussian();
  }

  public static void main(String args[])
      throws Exception {
    //Generate samples
    double []samples = new double[100000];
    for(int i=0; i<100000; i++){
      samples[i] = StandardNormalDistribution();
    }

    Connection conn = DriverManager.
        getConnection("jdbc:h2:mem:", "sowone", "");

    // Creat database
    String create = "CREATE TABLE sowone (SAMPLE DOUBLE PRIMARY KEY, VAR DOUBLE)";
    PreparedStatement preparedStmt = conn.prepareStatement(create);
    preparedStmt.execute();

    //Calculate Xi (mean)
    Statement st = conn.createStatement();
    Double sum=0.0;
    for(int i=0; i<100000; i++){
      sum += samples[i];
    }
    Double Xi = sum/100000;

    //Add samples and corresponding var in the data set (2 columns: Sample Var)
    for(int i=0; i<100000; i++){
      st.executeUpdate("INSERT INTO sowone (sample,var) "
          +"VALUES "+ "("+samples[i] +", "+ Math.pow(samples[i]-Xi,2) +")");
    }

    //Extract the avg of var -> the estimate of standard deviation of Xi
    PreparedStatement ps=conn.prepareStatement("SELECT AVG(var) " + "AS VAR "+
        "FROM sowone;");
    ResultSet r=ps.executeQuery();
    ResultSetMetaData data = r.getMetaData();

    conn.close();

  }
}
