package de.frostbyteger.pong.engine.graphics.ui;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;

import de.frostbyteger.pong.engine.graphics.FontHelper;

/**
 * @author Kevin
 * TODO: Add all functionalities
 *
 */
public class Box{
	
	// Box objects
	private ArrayList<ArrayList<Cell>> cells;
	private GameContainer parentContainer;
	private UnicodeFont boxFont;
	private ArrayList<Cell> sources;
	private Cell header   = null;

	// Box options
	private int boxWidth;
	private int boxHeight;
	private int boxX;
	private int boxY;
	private int boxFontSize;
	private boolean active       = true;
	private boolean visible      = true;
	private boolean clickable    = true;
	private boolean edged        = false;



	/**
	 * 
	 * @param boxWidth Width of the box
	 * @param boxHeight Height of the box
	 * @param boxX The boxes starting coordinations
	 * @param boxY the boxes starting coordinations
	 * @param boxFontPath
	 * @param boxFontSize
	 * @param cellWidth
	 * @param cellHeight
	 * @param container
	 * @throws SlickException
	 */
	public Box(int boxWidth, int boxHeight, int boxX, int boxY, String boxFontPath, int boxFontSize, float cellWidth, float cellHeight, GameContainer container) throws SlickException {
		this.parentContainer = container;
		this.sources = new ArrayList<Cell>();
		if(boxWidth <= 0 || boxHeight <= 0){
			throw new IllegalBoxArgumentException("Boxes width or height is 0 or below!");
		}
		if(cellWidth <= 0 || cellHeight <= 0){
			throw new IllegalCellArgumentException("Cell width or height is 0 or below!");
		}
		this.boxWidth = boxWidth;
		this.boxHeight = boxHeight;
		this.boxX = boxX;
		this.boxY = boxY;
		int cellX = boxX;
		int cellY = boxY;
		this.boxFontSize = boxFontSize;
		this.boxFont = FontHelper.newFont(boxFontPath, boxFontSize, false, false);
		this.cells = new ArrayList<ArrayList<Cell>>();
		this.header = new Cell(cellX, cellY - 50, cellWidth * boxWidth, 50, container);
		this.header.setFontPath(boxFontPath);
		this.header.setSize(boxFontSize);
		this.header.createNewFont();
		this.header.setEdging(true);
		this.header.setActive(false);
		this.header.setClickable(false);

		ArrayList<Cell> tempCell = new ArrayList<Cell>();
		
		for(int i = 0;i < boxWidth;i++){
			for(int j = 0; j < boxHeight;j++){
				tempCell.add(new Cell(cellX, cellY, cellWidth, cellHeight, container));
				cellY += cellHeight;
			}
			this.cells.add(tempCell);
			tempCell = new ArrayList<Cell>();
			cellX += cellWidth;
			cellY -= cellHeight * boxHeight;
		}
		int x = 1;
		for(int k = 0; k < this.cells.size();k++){
			for(int l = 0; l < this.cells.get(k).size();l++){
				Cell temp = this.cells.get(k).get(l);
				temp.setFontPath(boxFontPath);
				temp.setSize(boxFontSize);
				temp.createNewFont();
				temp.setCellText("Button" + x); //TODO: Delete
				this.sources.add(temp);
				x++;
			}
		}
		
	}
	
	public void render() throws SlickException{
		if(header.isActive() == true){
			header.drawCell();
		}
		for(int i = 0; i < cells.size();i++){
			for(int j = 0; j < cells.get(i).size();j++){
					cells.get(i).get(j).drawCell();
			}
		}
	}
	
	public void update(){
		
	}
	
	
	public static int showOptionBox(Rectangle overlay, Box box,GameContainer container, String message, BoxOptionSelection boxOS) throws SlickException{
		container.getGraphics().setColor(Color.darkGray);
		box.setHeaderActive(true);
		box.setHeaderEdging(true);
		box.setHeaderActive(true);
		box.setHeaderClickable(false);
		box.setHeaderTitle(message);
		container.getGraphics().fill(overlay);
		if(boxOS == BoxOptionSelection.YES_NO_CANCEL_BOX){
			box.getSources().get(0).setCellText("Yes");
			box.getSources().get(1).setCellText("No");
			box.getSources().get(2).setCellText("Cancel");
			box.render();
		}else if(boxOS == BoxOptionSelection.YES_NO_BOX){
			box.render();
		}

		return 0;
	}
	
	public static int showMessageBox(GameContainer container, String message, BoxOptionSelection boxOS){
		if(boxOS == BoxOptionSelection.OK_CANCEL_BOX){
			
		}else if(boxOS == BoxOptionSelection.OK_BOX){
			
		}
		
		return 0;
	}

	/**
	 * @return the cells
	 */
	public ArrayList<ArrayList<Cell>> getCells() {
		return cells;
	}

	/**
	 * @param cells the cells to set
	 */
	public void setCells(ArrayList<ArrayList<Cell>> cells) {
		this.cells = cells;
	}

	/**
	 * @return the sources
	 */
	public ArrayList<Cell> getSources() {
		return sources;
	}

	/**
	 * @return the boxFont
	 */
	public UnicodeFont getBoxFont() {
		return boxFont;
	}

	/**
	 * @param boxFont the boxFont to set
	 */
	public void setBoxFont(UnicodeFont boxFont) {
		this.boxFont = boxFont;
	}

	/**
	 * @return the boxFontSize
	 */
	public int getBoxFontSize() {
		return boxFontSize;
	}

	/**
	 * @param boxFontSize the boxFontSize to set
	 */
	public void setBoxFontSize(int boxFontSize) {
		this.boxFontSize = boxFontSize;
	}

	/**
	 * @return the boxWidth
	 */
	public int getBoxWidth() {
		return boxWidth;
	}

	/**
	 * @param boxWidth the boxWidth to set
	 */
	public void setBoxWidth(int boxWidth) {
		this.boxWidth = boxWidth;
	}

	/**
	 * @return the boxHeight
	 */
	public int getBoxHeight() {
		return boxHeight;
	}

	/**
	 * @param boxHeight the boxHeight to set
	 */
	public void setBoxHeight(int boxHeight) {
		this.boxHeight = boxHeight;
	}

	/**
	 * @return the boxX
	 */
	public int getBoxX() {
		return boxX;
	}

	/**
	 * @param boxX the boxX to set
	 */
	public void setBoxX(int boxX) {
		this.boxX = boxX;
	}

	/**
	 * @return the boxY
	 */
	public int getBoxY() {
		return boxY;
	}

	/**
	 * @param boxY the boxY to set
	 */
	public void setBoxY(int boxY) {
		this.boxY = boxY;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * @return the clickable
	 */
	public boolean isClickable() {
		return clickable;
	}

	/**
	 * @param clickable the clickable to set
	 */
	public void setClickable(boolean clickable) {
		this.clickable = clickable;
	}

	/**
	 * @return the edged
	 */
	public boolean isEdged() {
		return edged;
	}

	/**
	 * @param edged the edged to set
	 */
	public void setEdged(boolean edged) {
		this.edged = edged;
	}

	/**
	 * @return the parentContainer
	 */
	public GameContainer getParentContainer() {
		return parentContainer;
	}

	/**
	 * @param parentContainer the parentContainer to set
	 */
	public void setParentContainer(GameContainer parentContainer) {
		this.parentContainer = parentContainer;
	}

	/**
	 * @return the header
	 */
	public Cell getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(Cell header) {
		this.header = header;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isHeaderActive(){
		return this.header.isActive();
	}
	
	/**
	 * 
	 * @param active
	 */
	public void setHeaderActive(boolean active){
		this.header.setActive(active);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getHeaderTitle(){
		return this.header.getCellText();
	}
	
	/**
	 * 
	 * @param title
	 */
	public void setHeaderTitle(String title){
		this.header.setCellText(title);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isHeaderEdged(){
		return this.header.isEdging();
	}
	
	/**
	 * 
	 * @param edging
	 */
	public void setHeaderEdging(boolean edging){
		this.header.setEdging(edging);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isHeaderClickable(){
		return this.header.isClickable();
	}
	
	/**
	 * 
	 * @param clickable
	 */
	public void setHeaderClickable(boolean clickable){
		this.header.setClickable(clickable);
	}
	


}
