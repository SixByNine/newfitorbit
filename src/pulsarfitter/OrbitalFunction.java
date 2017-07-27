/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pulsarfitter;

import jaolho.data.lma.LMAFunction;

/**
 *
 * @author mkeith
 */
public class OrbitalFunction extends LMAFunction {

    private OrbitalParameters p;

    public OrbitalFunction(OrbitalParameters p) {
        this.p = p;
    }

    @Override
    public double getY(double x, double[] a) {
        p.setOrbitalParameters(a);

        double t = x - p.epoch; // days!

        double y = 0;

        // Period
        y += p.p0;

        // Pdot
        y += p.p1 * t*86400;

        //Binary Parameters
        
        double omB = 2*Math.PI/(p.pb*86400.0); // omB in seconds!
        
        double easc=2.0*Math.atan(Math.sqrt((1+p.e)/(1-p.e)) * Math.tan(Math.toRadians(-p.omega*0.5)));
        double epochPeri = p.epoch - (easc - p.e*Math.sin(easc)) * p.pb / (2.0*Math.PI);


        //First we need the mean anomaly
        double meanAnom = 2.0*Math.PI*(x-epochPeri)/p.pb;
        //eccentric anomaly
        double eccAnom = this.getEccentricAnomaly(meanAnom, p.e);

        double trueAnom = 2.0*Math.atan(Math.sqrt((1+p.e)/(1-p.e))  * Math.tan(eccAnom*0.5));

        // I'm pretty sure that v has units of "fraction of c" since x is in lt-s.
        double v = omB * p.x/Math.sqrt(1-p.e*p.e) * (Math.cos(trueAnom + Math.toRadians(p.omega)) + p.e * Math.cos(Math.toRadians(p.omega)));
        



//        

        y *= (1.0 + v);

        return y;
    }

    @Override
    public double getPartialDerivate(double x, double[] a, int parameterIndex) {

        // default! Should add better ones for p0,p1 etc.

        double y1=getY(x, a);
        double b[] = a.clone();
        b[parameterIndex]+=1e-10*a[parameterIndex];
        if(b[parameterIndex]-a[parameterIndex] == 0) b[parameterIndex]=1e-5;

        double y2=getY(x, b);

        return (y2-y1)/(b[parameterIndex] - a[parameterIndex]);

    }


     private double getEccentricAnomaly(double meanAnom, double ecc) {
        final double eacc = 1e-10; // the tollerence of the solution

        double ea=meanAnom;

        for (int i=0; i < 10 ; i++){
            double eNext = ea - (ea - p.e * Math.sin(ea) - meanAnom) / (1.0 - p.e * Math.cos(ea));

            if(Math.abs(eNext-ea) < eacc)break;
            ea=eNext;
        }

        return ea;
    }
}
