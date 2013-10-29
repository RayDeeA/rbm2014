package de.htw.cbir.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import de.htw.cbir.gui.DoubleJSlider;

public class Settings implements ChangeListener {

	public static enum SettingOption { LUM };
	
	protected EventListenerList listenerList = new EventListenerList();
	private double lumValue;

	public double getLumValue() {
		return lumValue;
	}
	
	public void setLumValue(double d) {
		lumValue = d;
	}

	/**
	 * Binding für die verschiedenen UI Elemente
	 * 
	 * @param option
	 * @param slider
	 */
	public void bind(final SettingOption option, JSlider slider) {
		slider.setName(option.toString());
		slider.addChangeListener(this);		
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		
		JComponent jComp = (JComponent) e.getSource();
		SettingOption option = SettingOption.valueOf(jComp.getName());
		
		switch (option) {
			case LUM:
				DoubleJSlider slider = (DoubleJSlider)jComp;
				if(!slider.getValueIsAdjusting()) {
					lumValue = slider.getDoubleValue();
					fireEvent(new ActionEvent(this, 1, option.toString()));
				}
				break;
	
			default:
				break;
		}
	}
	
	private void fireEvent(ActionEvent ev) {
		Object[] listeners = listenerList.getListenerList();
	    for (int i = 0; i < listeners.length; i = i+2) {
	      if (listeners[i] == ActionListener.class) {
	        ((ActionListener) listeners[i+1]).actionPerformed(ev);
	      }
	    }
	}
	
	public void addChangeListener(ActionListener listener) {
		listenerList.add(ActionListener.class, listener);
	}
}
