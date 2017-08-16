package com.teej107.netsearch.swing;

import com.teej107.netsearch.Application;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by teej107 on 8/1/2017.
 */
public class StringJList extends JScrollPane implements MouseMotionListener, MouseListener
{
	private static final Color LABEL_COLOR = new Color(10, 10, 10);
	private static final Color HOVER_COLOR = new Color(240, 240, 240, 255);
	private static final Color SELECTED_COLOR = new Color(255, 255, 255, 255);
	private static final Color NORMAL_COLOR = new Color(255, 255, 255, 200);

	private boolean hideIfNoData;
	private JList<JLabel> jList;
	private Model model;
	private List<JLabel> data;
	private int cellSize, hoverCell;
	private Border border;
	private ChangeListener changeListener;

	public StringJList(Border border, boolean hideIfNoData, int cellSize)
	{
		this.border = border;
		this.hideIfNoData = hideIfNoData;
		this.cellSize = cellSize;
		this.data = new ArrayList<>();
		this.jList = new JList<>();
		this.model = new Model();
		this.jList.setModel(model);
		this.jList.setCellRenderer(new CellRenderer());
		this.jList.addMouseMotionListener(this);
		this.jList.addMouseListener(this);
		this.jList.setOpaque(false);
		if (hideIfNoData)
		{
			setVisible(false);
		}
		this.hoverCell = -1;
		setViewportView(jList);
	}

	@Override
	public void setOpaque(boolean isOpaque)
	{
		super.setOpaque(isOpaque);
		getViewport().setOpaque(isOpaque);
	}

	public void addSelectionListener(ListSelectionListener listener)
	{
		jList.addListSelectionListener(listener);
	}

	public void setSelectedIndex(int i)
	{
		ListSelectionListener[] listeners = jList.getListSelectionListeners();
		for(ListSelectionListener listener : listeners)
		{
			jList.removeListSelectionListener(listener);
		}
		jList.setSelectedIndex(i);
		for(ListSelectionListener listener : listeners)
		{
			jList.addListSelectionListener(listener);
		}
	}

	public int getSelectedIndex()
	{
		return jList.getSelectedIndex();
	}

	public int getTotalHeight()
	{
		if(data.size() == 0)
			return 0;

		JLabel label = data.get(0);
		return (label.getPreferredSize().height + 1) * data.size();
	}

	public int getMaxIndex()
	{
		return data.size() - 1;
	}

	public String getSelectedString()
	{
		int index = jList.getSelectedIndex();
		if(index == -1)
			return null;

		return data.get(index).getText();
	}

	@Override
	public synchronized void addMouseListener(MouseListener l)
	{
		jList.addMouseListener(l);
	}

	@Override
	public synchronized void addMouseMotionListener(MouseMotionListener l)
	{
		jList.addMouseMotionListener(l);
	}

	public void setChangeListener(ChangeListener listener)
	{
		this.changeListener = listener;
	}

	public boolean isEmpty()
	{
		return data.isEmpty();
	}

	public void setData(List<String> list)
	{
		this.data.clear();


		if(list != null)
		{
			Font font = getFont();
			int fontSize = Application.calculateFontSize(getFontMetrics(font), cellSize);
			font = font.deriveFont((float) fontSize);
			for (String s : list)
			{
				JLabel label = new JLabel(s, JLabel.LEFT);
				label.setOpaque(true);
				label.setForeground(LABEL_COLOR);
				label.setFont(font);

				data.add(label);
			}
		}
		if (hideIfNoData)
		{
			setVisible(list != null && list.size() > 0);
		}
		model.update();
		changeListener.stateChanged(new ChangeEvent(this));
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{

	}

	@Override
	public void mousePressed(MouseEvent e)
	{

	}

	@Override
	public void mouseReleased(MouseEvent e)
	{

	}

	@Override
	public void mouseEntered(MouseEvent e)
	{

	}

	@Override
	public void mouseExited(MouseEvent e)
	{

	}

	@Override
	public void mouseDragged(MouseEvent e)
	{

	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		int index = jList.locationToIndex(e.getPoint());
		if (index != hoverCell)
		{
			hoverCell = index;
			jList.repaint();
		}
	}

	private class Model extends AbstractListModel<JLabel>
	{
		@Override
		public int getSize()
		{
			return data == null ? 0 : data.size();
		}

		@Override
		public JLabel getElementAt(int index)
		{
			return data == null ? null : data.get(index);
		}

		public void update()
		{
			fireContentsChanged(jList, 0, getSize());
		}
	}

	private class CellRenderer implements ListCellRenderer<JLabel>
	{
		@Override
		public Component getListCellRendererComponent(JList<? extends JLabel> list, JLabel value, int index, boolean isSelected,
				boolean cellHasFocus)
		{
			if (isSelected)
			{
				value.setBackground(SELECTED_COLOR);
			}
			else if (index == hoverCell)
			{
				value.setBackground(HOVER_COLOR);
			}
			else
			{
				value.setBackground(NORMAL_COLOR);
			}
			value.setBorder(getBorder(getBorderColor(border), index, list.getModel().getSize()));
			return value;
		}
	}

	private static Border getBorder(Color color, int index, int size)
	{
		if(color == null)
			return null;
		return BorderFactory.createMatteBorder(index == 0 ? 1 : 0, 1, index == size - 1 ? 1 : 0, 1, color);
	}

	private static Color getBorderColor(Border border)
	{
		if(border == null)
			return null;
		if(border instanceof MatteBorder)
			return ((MatteBorder) border).getMatteColor();
		if(border instanceof LineBorder)
			return ((LineBorder) border).getLineColor();

		return Color.GRAY;
	}
}
