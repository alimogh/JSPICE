package com.JSPICE.SElement;

import com.JSPICE.Util.ComponentTerminals;
import com.JSPICE.SMath.*;
import com.JSPICE.Util.ComponentDenominations;

/**
 * @author 1sand0s
 *
 */
public class Resistor extends SElement {

    private double resistance;

    public Resistor() {
        denomination = ComponentDenominations.R;
        resistance = 0;
        terminals = new Terminals(2,
                new ComponentTerminals[] { ComponentTerminals.POS_NODE, ComponentTerminals.NEG_NODE });
    }

    @Override
    public double getValue() {
        return resistance;
    }

    @Override
    public void setValue(double value) {
        resistance = value;
    }

    @Override
    public Complex[] getVoltage(Complex[][] result){
        int posNode = terminals.getTerminal(ComponentTerminals.POS_NODE);
        int negNode = terminals.getTerminal(ComponentTerminals.NEG_NODE);

        return (ComplexMatrixOperations.SubArrays(result[posNode],
						  result[negNode]));
    }

    @Override
    public Complex[] getCurrent(Complex[][] result,
				double frequency) {
        return ComplexMatrixOperations.ScalarMultiply(getVoltage(result),
						      1 / resistance);
    }

    @Override
    public void stampMatrixDC(Complex[][] G,
                              Complex[][] B,
                              Complex[][] C,
                              Complex[][] D,
                              Complex[][] z,
                              int iSourceIndex) {
        int posNode = terminals.getTerminal(ComponentTerminals.POS_NODE);
        int negNode = terminals.getTerminal(ComponentTerminals.NEG_NODE);

        G[posNode][posNode].add(new Complex(1 / resistance, 0));
        G[negNode][negNode].add(new Complex(1 / resistance, 0));
        G[posNode][negNode].add(new Complex(-1 / resistance, 0));
        G[negNode][posNode].add(new Complex(-1 / resistance, 0));
    }

    @Override
    public void stampMatrixAC(Complex[][] G,
                              Complex[][] B,
                              Complex[][] C,
                              Complex[][] D,
                              Complex[][] z,
                              int iSourceIndex,
                              double frequency) {
        stampMatrixDC(G, B, C, D, z, iSourceIndex);
    }
}
