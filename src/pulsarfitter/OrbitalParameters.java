/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pulsarfitter;

/**
 *
 * @author mkeith
 */
public class OrbitalParameters implements Cloneable {
    // Parameters

    double epoch; // MJD
    boolean fitEpoch = false;
    double p0; // s
    boolean fitP0 = false;
    double p1; // s/s
    boolean fitP1 = false;
    double pb; // days
    boolean fitPb = false;
    double x; // lt-s
    boolean fitX = false;
    double tasc; // 
    boolean fitTasc = false;
    double omega; // 
    boolean fitOmega = false;
    double e; // no units
    boolean fitE = false;

    public OrbitalParameters(double epoch, double p0, double p1, double pb, double x, double tasc, double omega, double e) {
        this.epoch = epoch;
        this.p0 = p0;
        this.p1 = p1;
        this.pb = pb;
        this.x = x;
        this.tasc = tasc;
        this.omega = omega;
        this.e = e;
    }

    public void setOrbitalParameters(double[] a) {
        int i = 0;

        if (fitEpoch) {
            this.epoch = a[i++];
        }
        if (fitP0) {
            this.p0 = a[i++];
        }
        if (fitP1) {
            this.p1 = a[i++];
        }
        if (fitPb) {
            this.pb = a[i++];
        }
        if (fitX) {
            this.x = a[i++];
        }
        if (fitTasc) {
            this.tasc = a[i++];
        }
        if (fitOmega) {
            this.omega = a[i++];
        }
        if (fitE) {
            this.e = a[i++];
        }

	sanify();
    }

    public double[] getParmArray() {
        int i = 0;
        if (fitP0) {
            i++;
        }
        if (fitEpoch) {
            i++;
        }
        if (fitP1) {
            i++;
        }
        if (fitPb) {
            i++;
        }
        if (fitX) {
            i++;
        }
        if (fitTasc) {
            i++;
        }
        if (fitOmega) {
            i++;
        }
        if (fitE) {
            i++;
        }

        double[] a = new double[i];

	sanify();
        i = 0;
        if (fitEpoch) {
            a[i++] = this.epoch;
        }
        if (fitP0) {
            a[i++] = this.p0;
        }
        if (fitP1) {
            a[i++] = this.p1;
        }
        if (fitPb) {
            a[i++] = this.pb;
	    if(a[i-1]==0)a[i-1]=1;
        }
        if (fitX) {
            a[i++] = this.x;
        }
        if (fitTasc) {
            a[i++] = this.tasc;
        }
        if (fitOmega) {
            a[i++] = this.omega;
        }
        if (fitE) {
            a[i++] = this.e;
        }

        return a;
    }


    public void sanify(){
	while(this.omega > 360.0)this.omega-=360.0;
	while(this.omega < 0.0)this.omega+=360.0;
    }
    public double[] getAllParamArray() {


        double[] a = new double[8];

        int i = 0;
	sanify();

        a[i++] = this.epoch;
        a[i++] = this.p0;
        a[i++] = this.p1;
        a[i++] = this.pb;
        a[i++] = this.x;
        a[i++] = this.tasc;
        a[i++] = this.omega;
        a[i++] = this.e;

        return a;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
