/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pulsarfitter;

import jaolho.data.lma.LMA;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mkeith
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String inpar = args[0];
        String inper = args[1];

        OrbitalParameters param = null;
            Parfile par = new Parfile();
        try {
            par.read(new File(inpar));

            param = new OrbitalParameters(par.getT0(), 1.0 / par.getF0(), -par.getF1() / (par.getF0() * par.getF0()), par.getPb(), par.getA1(), par.getTasc(), par.getOmega(), par.getEcc());

        } catch (IOException iOException) {
            iOException.printStackTrace();
        }
        Perfile per = new Perfile();
        try {
            per.read(new File(inper));
        } catch (IOException iOException) {
            iOException.printStackTrace();
        }

        double[] periods=per.getPeriods();
        double[] mjds=per.getMjds();
        double[] errors=per.getPerrors();
        double[] weights=new double[errors.length];
        double start=10000000;
        double end=0;
        for (int i=0; i < errors.length ; i++){
            weights[i] = 1.0/errors[i];
            if(mjds[i] < start) start=mjds[i];
            if(mjds[i] > end) end=mjds[i];
        }

        OrbitalFunction func = new OrbitalFunction(param);
        PrintStream out = null;
        try {
            out = new PrintStream("prefit.plot");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        writeFitPlot(out,end, start, param, func);


        param.fitPb = true;
        param.fitP0 = true;
        param.fitX = true;
        param.fitEpoch = true;

        LMA fitter = new LMA(func, param.getParmArray(), new double[][]{mjds, periods});
        fitter.fit();
        System.out.println("RESULT PARAMETERS: " + Arrays.toString(fitter.parameters));

         param.setOrbitalParameters(fitter.parameters);
        try {
            out = new PrintStream("postfit.plot");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        writeFitPlot(out,end, start,param, func);



    }

    private static void writeFitPlot(PrintStream out, double end, double start, OrbitalParameters param, OrbitalFunction func) {
        int ncurvepts = (int) (128*(end-start)/param.pb);
        double[] xs = new double[ncurvepts];
        for (int i = 0; i < ncurvepts; i++) {
            xs[i] = start + i * param.pb / 128;
        }
        double[] ys = func.generateData(xs, param.getParmArray());
        for (int i = 0; i < ncurvepts; i++) {
            out.println(xs[i] + "\t" + ys[i]);
        }
    }
}
