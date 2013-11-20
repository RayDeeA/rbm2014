package de.htw.cbir.model;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import de.htw.cbir.gui.DoubleJSlider;

public class Settings implements ChangeListener {

	public static enum SettingOption {
		LUMINANCE, INPUT_SIZE, OUTPUT_SIZE, LEARN_RATE, EPOCHS, UPDATE_FREQUENCY, SEED
	};

	protected EventListenerList listenerList = new EventListenerList();
	private double luminance = 0;
	private int inputSize = 15;
	private int outputSize = 4;
	private double learnRate = 0.1;
	private int epochs = 10000;
	private int updateFrequency = 100;
	private boolean useSeed = false;
	private int seed = 0;

	public double getLuminance() {
		return luminance;
	}

	public void setLuminance(double d) {
		luminance = d;
	}

	public int getInputSize() {
		return inputSize;
	}

	public void setInputSize(int inputSizeValue) {
		this.inputSize = inputSizeValue;
	}
	

	public int getOutputSize() {
		return outputSize;
	}

	public void setOutputSize(int outputSizeValue) {
		this.outputSize = outputSizeValue;
	}

	public double getLearnRate() {
		return learnRate;
	}

	public void setLearnRate(double learnRate) {
		this.learnRate = learnRate;
	}

	public int getEpochs() {
		return epochs;
	}

	public void setEpochs(int epochs) {
		this.epochs = epochs;
	}

	public int getUpdateFrequency() {
		return updateFrequency;
	}

	public void setUpdateFrequency(int updateFrequency) {
		this.updateFrequency = updateFrequency;
	}

	public boolean isUseSeed() {
		return useSeed;
	}

	public void setUseSeed(boolean useSeed) {
		this.useSeed = useSeed;
	}

	public int getSeed() {
		return seed;
	}

	public void setSeed(int seed) {
		this.seed = seed;
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
	
	public void bind(final SettingOption option, JRadioButtonMenuItem radioButton) {
		radioButton.setName(option.toString());
		radioButton.addChangeListener(this);
	}
	
	public String getSelectedButtonText(JMenu menu) {
		
		for (Component child : menu.getComponents()) {
			if (child instanceof JRadioButtonMenuItem) {
				JRadioButtonMenuItem radioButton = (JRadioButtonMenuItem)child;
				
	            if (radioButton.isSelected()) {
	                return radioButton.getText();
	            }
			}
		}
        return null;
    }

	@Override
	public void stateChanged(ChangeEvent e) {

		JComponent jComp = (JComponent) e.getSource();
		SettingOption option = SettingOption.valueOf(jComp.getName());

		switch (option) {
		case LUMINANCE:
			DoubleJSlider slider = (DoubleJSlider) jComp;
			if (!slider.getValueIsAdjusting()) {
				setLuminance(slider.getDoubleValue());
				fireEvent(new ActionEvent(this, 1, option.toString()));
			}
			break;
		case LEARN_RATE:
			DoubleJSlider learnRateslider = (DoubleJSlider) jComp;
			if (!learnRateslider.getValueIsAdjusting()) {
				setLearnRate(learnRateslider.getDoubleValue());
				fireEvent(new ActionEvent(this, 1, option.toString()));
			}
			break;
		case INPUT_SIZE:
			JSlider inputSizeSlider = (JSlider) jComp;
			if (!inputSizeSlider.getValueIsAdjusting()) {
				setInputSize(inputSizeSlider.getValue());
				fireEvent(new ActionEvent(this, 1, option.toString()));
			}
			break;
		case OUTPUT_SIZE:
			JSlider outputSizeSlider = (JSlider) jComp;
			if (!outputSizeSlider.getValueIsAdjusting()) {
				setOutputSize(outputSizeSlider.getValue());
				fireEvent(new ActionEvent(this, 1, option.toString()));
			}
			break;
		case UPDATE_FREQUENCY:
			JSlider updateFrequencySlider = (JSlider) jComp;
			if (!updateFrequencySlider.getValueIsAdjusting()) {
				setUpdateFrequency(updateFrequencySlider.getValue());
				fireEvent(new ActionEvent(this, 1, option.toString()));
			}
			break;
		case EPOCHS:
			JSlider epochsSlider = (JSlider) jComp;
			if (!epochsSlider.getValueIsAdjusting()) {
				setEpochs(epochsSlider.getValue() * 1000);
				fireEvent(new ActionEvent(this, 1, option.toString()));
			}
			break;
			
		case SEED: 
			JRadioButtonMenuItem radioButton = (JRadioButtonMenuItem) jComp;
			
			if(radioButton.isSelected()) {
				String valueString = radioButton.getText();
				if(valueString.equals("NO")) {
					setUseSeed(false);
					setSeed(0);
				} else {
					setUseSeed(true);
					setSeed(Integer.parseInt(valueString));
				}
				fireEvent(new ActionEvent(this, 1, option.toString()));
			}
			
			break;
			
		default:
			break;
		}
	}

	private void fireEvent(ActionEvent ev) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i = i + 2) {
			if (listeners[i] == ActionListener.class) {
				((ActionListener) listeners[i + 1]).actionPerformed(ev);
			}
		}
	}

	public void addChangeListener(ActionListener listener) {
		listenerList.add(ActionListener.class, listener);
	}
}
