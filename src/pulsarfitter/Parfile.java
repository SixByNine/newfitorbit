/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pulsarfitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;

/**
 *
 * @author kei041
 */
public class Parfile {

    private String psrj = "VOID";
    private String raj = "00:00:00";
    private String decj = "00:00:00";
    private double f0 = 1;
    private double f1 = 0;
    private double pepoch = 50000;
    private double dm = 0;
    private double pb = 0;
    private double t0 = 0;
    private double a1 = 0;
    private double tasc = 0;
    private double omega = 0;
    private double ecc = 0;

    public void read(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        while (line != null) {
            String[] elems = line.split("\\s\\s*");
            Field f = null;
            try {
                f = Parfile.class.getDeclaredField(elems[0].toLowerCase());
            } catch (Exception e) {
		if(elems.length > 1){
			if (elems[0].equalsIgnoreCase("P0")) {
				this.f0 = 1.0 / Double.parseDouble(elems[1]);
			}
			if (elems[0].equalsIgnoreCase("P1")) {
				this.f1 = -f0 * f0 * Double.parseDouble(elems[1]);
			}
			if (elems[0].equalsIgnoreCase("E")) {
				this.ecc = Double.parseDouble(elems[1]);
			}
			if (elems[0].equalsIgnoreCase("OM")) {
				this.omega = Double.parseDouble(elems[1]);
			}

		}
                line = reader.readLine();
                continue;
            }
            try {
                if (f.getType() == double.class) {
                    f.set(this, Double.parseDouble(elems[1]));
                }
                if (f.getType() == String.class) {
                    f.set(this, elems[1]);
                }
            } catch (IllegalArgumentException illegalArgumentException) {
            } catch (IllegalAccessException illegalAccessException) {
            }
            line = reader.readLine();


        }
    }


    public void write(String file) throws IOException{
        PrintStream out = new PrintStream(file);
        out.printf("PSRJ           %s\n", this.getPsrj());
        out.printf("RAJ            %s\n", this.getRaj());
        out.printf("DECJ           %s\n", this.getDecj());
        out.printf("F0             %16.14f\n", this.getF0());
        out.printf("F1             %g\n", this.getF1());
        out.printf("PEPOCH         %16.12f\n", this.getPepoch());
        out.printf("DM             %f\n",this.getDm());
        out.printf("BINARY         BT\n");
        out.printf("PB             %16.14f\n",this.getPb());
        out.printf("A1             %16.14f\n",this.getA1());
        out.printf("OM             %16.12f\n",this.getOmega());
        out.printf("T0             %16.12f\n",this.getT0());
        out.printf("ECC            %g\n",this.getEcc());
        out.close();
        out = new PrintStream(file+".ell1");
        out.printf("PSRJ           %s\n", this.getPsrj());
        out.printf("RAJ            %s\n", this.getRaj());
        out.printf("DECJ           %s\n", this.getDecj());
        out.printf("F0             %16.14f\n", this.getF0());
        out.printf("F1             %g\n", this.getF1());
        out.printf("PEPOCH         %16.12f\n", this.getPepoch());
        out.printf("DM             %f\n",this.getDm());
        out.printf("BINARY         ELL1\n");
        out.printf("PB             %16.14f\n",this.getPb());
        out.printf("A1             %16.14f\n",this.getA1());
        out.printf("TASC           %16.12f\n",this.getT0());
        out.printf("EPS1           %16.12f\n",this.getEcc()*Math.cos(Math.toRadians(this.getOmega())));
        out.printf("EPS2           %16.12f\n",this.getEcc()*Math.sin(Math.toRadians(this.getOmega())));
	out.close();
    }

    /**
     * @return the psrj
     */
    public String getPsrj() {
        return psrj;
    }

    /**
     * @param psrj the psrj to set
     */
    public void setPsrj(String psrj) {
        this.psrj = psrj;
    }

    /**
     * @return the raj
     */
    public String getRaj() {
        return raj;
    }

    /**
     * @param raj the raj to set
     */
    public void setRaj(String raj) {
        this.raj = raj;
    }

    /**
     * @return the decj
     */
    public String getDecj() {
        return decj;
    }

    /**
     * @param decj the decj to set
     */
    public void setDecj(String decj) {
        this.decj = decj;
    }

    /**
     * @return the f0
     */
    public double getF0() {
        return f0;
    }

    /**
     * @param f0 the f0 to set
     */
    public void setF0(double f0) {
        this.f0 = f0;
    }

    /**
     * @return the f1
     */
    public double getF1() {
        return f1;
    }

    /**
     * @param f1 the f1 to set
     */
    public void setF1(double f1) {
        this.f1 = f1;
    }

    /**
     * @return the pepoch
     */
    public double getPepoch() {
        return pepoch;
    }

    /**
     * @param pepoch the pepoch to set
     */
    public void setPepoch(double pepoch) {
        this.pepoch = pepoch;
    }

    /**
     * @return the dm
     */
    public double getDm() {
        return dm;
    }

    /**
     * @param dm the dm to set
     */
    public void setDm(double dm) {
        this.dm = dm;
    }

    /**
     * @return the pb
     */
    public double getPb() {
        return pb;
    }

    /**
     * @param pb the pb to set
     */
    public void setPb(double pb) {
        this.pb = pb;
    }

    /**
     * @return the t
     */
    public double getT0() {
        return t0;
    }

    /**
     * @param t the t to set
     */
    public void setT0(double t) {
        this.t0 = t;
    }


    /**
     * @return the tasc
     */
    public double getTasc() {
        return tasc;
    }

    /**
     * @param tasc the tasc to set
     */
    public void setTasc(double tasc) {
        this.tasc = tasc;
    }

    /**
     * @return the omega
     */
    public double getOmega() {
        return omega;
    }

    /**
     * @param omega the omega to set
     */
    public void setOmega(double omega) {
        this.omega = omega;
    }

    /**
     * @return the ecc
     */
    public double getEcc() {
        return ecc;
    }

    /**
     * @param ecc the ecc to set
     */
    public void setEcc(double ecc) {
        this.ecc = ecc;
    }

    /**
     * @return the a0
     */
    public double getA1() {
        return a1;
    }

    /**
     * @param a0 the a0 to set
     */
    public void setA0(double a1) {
        this.a1 = a1;
    }
}
