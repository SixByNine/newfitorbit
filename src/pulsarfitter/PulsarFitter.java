/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pulsarfitter;

import jaolho.data.lma.LMA;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 *
 * @author kei041
 */
public class PulsarFitter {

    private Perfile perfile = new Perfile();

    public Perfile getPerfile() {
        return perfile;
    }
    private Parfile parfile = new Parfile();

    public Parfile getParfile() {
        return parfile;
    }
    private OrbitalParameters params;
    private OrbitalParameters prevParams;

    public PulsarFitter() {
        params = new OrbitalParameters(0, 0, 0, 1, 0, 0, 0, 0);
        prevParams = new OrbitalParameters(0, 0, 1, 0, 0, 0, 0, 0);
    }

    public void loadPer(String per) throws IOException {
        perfile = new Perfile();
        perfile.read(new File(per));
        resetStartEnd();
    }

    public void loadPar(String par) throws IOException {
        parfile = new Parfile();
        parfile.read(new File(par));
        params = new OrbitalParameters(parfile.getT0(), 1.0 / parfile.getF0(), -parfile.getF1() / (parfile.getF0() * parfile.getF0()), parfile.getPb(), parfile.getA1(), parfile.getTasc(), parfile.getOmega(), parfile.getEcc());
    }

    public void writePar() {
        try {
            parfile.setA0(params.x);
            parfile.setEcc(params.e);
            parfile.setF0(1.0 / params.p0);
            parfile.setF1(-params.p1 / (params.p0 * params.p0));
            parfile.setOmega(params.omega);
            parfile.setPb(params.pb);
            parfile.setT0(params.epoch);
            parfile.setPepoch(params.epoch);
            parfile.setTasc(params.tasc);
            parfile.write("fitorbit.par");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void resetStartEnd() {
        this.setStart(0);
        this.setEnd(100000000);
        double start = 100000000;
        double end = 0;

        double[] mjds = perfile.getMjds();
        if (mjds.length == 0) {
            this.setStart(0);
            this.setEnd(1);
        }

        for (int i = 0; i < mjds.length; i++) {
            if (mjds[i] < start) {
                start = mjds[i];
            }
            if (mjds[i] > end) {
                end = mjds[i];
            }
        }

        double len = end - start;
        end += len / 100;
        start -= len / 100;
        this.setEnd(end);
        this.setStart(start);

    }

    public boolean refit() {
        double[] periods = perfile.getPeriods();
        double[] mjds = perfile.getMjds();
        double[] errors = perfile.getPerrors();
        double[] weights = new double[errors.length];
        for (int i = 0; i < mjds.length; i++) {
            weights[i] = 1.0 / errors[i];
        }
        try {
            prevParams = (OrbitalParameters) params.clone();
        } catch (CloneNotSupportedException ex) {
        }

        LMA fitter = new LMA(new OrbitalFunction(params), params.getParmArray(), new double[][]{mjds, periods});
        try {
            fitter.fit();
        } catch (Exception ex) {
            System.err.println(ex.getLocalizedMessage());
            return false;
        }

        params.setOrbitalParameters(fitter.parameters);
        return true;

    }

    public double[][] getFitCurve(int resn) {

        int ncurvepts = 2;
        if (params.pb > 0) {
            ncurvepts = (int) (resn * (this.getEnd() - this.getStart()) / params.pb);
        }
        if (ncurvepts < 1) {
            ncurvepts = 1;
        }
        double[] xs = new double[ncurvepts];
        for (int i = 0; i < ncurvepts; i++) {
            xs[i] = this.getStart() + i * params.pb / resn;
        }
        double[] ys = new OrbitalFunction(params).generateData(xs, params.getParmArray());
        return new double[][]{xs, ys};


    }

    public void writeFitPlot(PrintStream out, int resn) {
        double[][] xys = getFitCurve(resn);
        for (int i = 0; i <
                xys[0].length; i++) {
            out.println(xys[0][i] + "\t" + xys[1][i]);
        }

    }

    /**
     * @return the params
     */
    public OrbitalParameters getParams() {
        return params;
    }

    /**
     * @return the prevParams
     */
    public OrbitalParameters getPrevParams() {
        return prevParams;
    }

    /**
     * @return the start
     */
    public double getStart() {
        return this.perfile.getStart();
    }

    /**
     * @param start the start to set
     */
    public void setStart(double start) {
        this.getPerfile().setStart(start);
    }

    /**
     * @return the end
     */
    public double getEnd() {
        return this.perfile.getEnd();
    }

    /**
     * @param end the end to set
     */
    public void setEnd(double end) {
        this.getPerfile().setEnd(end);
    }

    void undoFit() {
        OrbitalParameters tmp = this.prevParams;
        this.prevParams = this.params;
        this.params = tmp;

    }
}
