/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pulsarfitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author kei041
 */
public class Perfile {

    private ArrayList<Double> mjds = new ArrayList<Double>();
    private ArrayList<Double> periods = new ArrayList<Double>();
    private ArrayList<Double> perrors = new ArrayList<Double>();
    private double start = 0;
    private double end = 1;
    private ArrayList<Integer> delete = new ArrayList<Integer>();

    public void read(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        while (line != null) {
            String[] elems = line.trim().split("\\s\\s*");

            double mjd;
            double period;
            double perr;

            try {
                mjd = Double.parseDouble(elems[0]);
                period = Double.parseDouble(elems[1]) / 1000.0;
                perr = Double.parseDouble(elems[2]) / 1000.0;
            } catch (Exception e) {
                line = reader.readLine();
                continue;
            }

            mjds.add(mjd);
            periods.add(period);
            perrors.add(perr);
            line = reader.readLine();
        }
    }

    public double[] getPeriods() {
        double[] out = new double[periods.size()];
        int c = 0;
        for (int i = 0; i < out.length; i++) {
            if (mjds.get(i) > start && mjds.get(i) < end && !delete.contains(i)) {
                out[c] = periods.get(i);
                c++;
            }
        }

        double[] out2 = new double[c];
        System.arraycopy(out, 0, out2, 0, c);
        return out2;
    }

    public double[] getMjds() {
        double[] out = new double[mjds.size()];
        int c = 0;
        for (int i = 0; i < out.length; i++) {
            if (mjds.get(i) > start && mjds.get(i) < end && !delete.contains(i)) {
                out[c] = mjds.get(i);
                c++;
            }
        }
        double[] out2 = new double[c];
        System.arraycopy(out, 0, out2, 0, c);
        return out2;
    }

    public double[] getPerrors() {
        double[] out = new double[perrors.size()];
        int c = 0;
        for (int i = 0; i < out.length; i++) {
            if (mjds.get(i) > start && mjds.get(i) < end && !delete.contains(i)) {
                out[c] = perrors.get(i);
                c++;
            }
        }
        double[] out2 = new double[c];
        System.arraycopy(out, 0, out2, 0, c);
        return out2;
    }


    public void delete(double tmin, double tmax, double pmin, double pmax) {
        for (int i = 0; i < mjds.size(); i++) {
            double t = mjds.get(i);
            double p = periods.get(i);
            if (t < tmax && t > tmin && p > pmin && p < pmax) {
                delete.add(i);
            }
        }
    }

    public void delete(double tc, double pc, double tmin, double tmax, double pmin, double pmax) {
        int c=-1;
        double distmin=Double.MAX_VALUE;
        double tscale=tmax-tmin;
        double pscale=pmax-pmin;

        for (int i = 0; i < mjds.size(); i++) {
            double t = mjds.get(i);
            double p = periods.get(i);
            if (t < tmax && t > tmin && p > pmin && p < pmax) {
                double dist = Math.sqrt(Math.pow((t - tc)/tscale,2) + Math.pow((t - tc)/tscale,2));
                if (dist < distmin){
                    distmin=dist;
                    c=i;
                }
            }
        }
        this.delete.add(c);
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public ArrayList<Integer> getDelete() {
        return delete;
    }
}
