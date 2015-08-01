package com.example.simulando;



import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;





import org.andengine.util.adt.color.*;
import org.andengine.util.debug.Debug;

import android.graphics.Typeface;

import com.example.simulando.MainActivity;


public class ResourcesManager {
	//---------------------------------------------
    // VARIABLES
    //---------------------------------------------
    
    private static final ResourcesManager INSTANCE = new ResourcesManager();

	
    
    public Engine engine;
    public MainActivity activity;
    public BoundCamera camera;
    public VertexBufferObjectManager vbom;
    private BitmapTextureAtlas splashTextureAtlas;
    
    ///////////textures
    public ITextureRegion menu_background_region;
    public ITextureRegion play_region;
    public ITextureRegion simulation_region;
    public ITextureRegion options_region;        
    public ITextureRegion splash_region;
    public ITextureRegion coin_region;
    public ITextureRegion platform1_region;
    public ITextureRegion platform2_region;
    public ITextureRegion repeating_background;
    public ITextureRegion simulationTextureRegionAngleButton;
    public ITextureRegion simulationTextureRegionAngleButtonHover;
    public ITextureRegion simulationTextureRegionSpeedButton;
    public ITextureRegion simulationTextureRegionSpeedButtonHover;
    public ITextureRegion simulationTextureRegionPlayButton;
    public ITextureRegion simulationTextureRegionPlayButtonHover;    
    public ITextureRegion simulationTextureRegionResetButton;
    public ITextureRegion simulationTextureRegionResetButtonHover;    
    public ITextureRegion simulationTextureRegionMoveButton;
    public ITextureRegion simulationTextureRegionMoveButtonHover;
    public ITextureRegion simulationTextureRegionProjectile;
    public ITextureRegion tileTextureRegion;
    public ITextureRegion lockRegion;
    public ITextureRegion lightRegion;
    public ITextureRegion brickRegion;
    public ITextureRegion boardRegion;
    
    private BuildableBitmapTextureAtlas menuTextureAtlas;
    private BuildableBitmapTextureAtlas menuBtnTextureAtlas;
  //  private BuildableBitmapTextureAtlas simulationTextureAtlas;
    private BuildableBitmapTextureAtlas gameTextureAtlas;
    private BitmapTextureAtlas simulationBGTextureAtlas;
    private BitmapTextureAtlas levelSelectorTileTextureAtlas;
    private BitmapTextureAtlas lightTextureAtlas;
    private BitmapTextureAtlas boardTextureAtlas;
    
    
    public Font font;
    public Font font_score;
    public Font font_score1;
    public Font simulationvariablesFont;



	private BitmapTextureAtlas simulationTextureAtlasAngleButton;
	private BitmapTextureAtlas simulationTextureAtlasSpeedButton;
	private BitmapTextureAtlas simulationTextureAtlasPlayButton;
	private BitmapTextureAtlas simulationTextureAtlasResetButton;
	private BitmapTextureAtlas simulationTextureAtlasMoveButton;
	private BitmapTextureAtlas simulationTextureAtlasProjectile;



	public Font fontLevelTile;
	




	



	


public void loadMenuResources()
{
    loadMenuGraphics();
    loadMenuAudio();
    loadMenuFonts();
}

private void loadMenuFonts() {
	font = FontFactory.create(activity.getFontManager(),
			activity.getTextureManager(), 256, 256,
			   Typeface.create(Typeface.DEFAULT, Typeface.BOLD),
			   64f, true,Color.BLUE_ABGR_PACKED_INT);
	font.load();
	
	fontLevelTile = FontFactory.create(activity.getFontManager(),
			activity.getTextureManager(), 256, 256,
			   Typeface.create(Typeface.SERIF, Typeface.BOLD),
			   20f, true,Color.BLACK_ABGR_PACKED_INT);
	fontLevelTile.load();
	
}
public void loadSimulationResources()
{
	loadSimulationGraphics();
    loadSimulationFonts();
    loadSimulationAudio();
}

private void loadSimulationAudio() {
	
	
}

private void loadSimulationFonts() {
	
	 FontFactory.setAssetBasePath("font/");
	font = FontFactory.create(activity.getFontManager(),
			activity.getTextureManager(), 512, 512,
			   Typeface.create(Typeface.DEFAULT, Typeface.BOLD),
			   80f, true,Color.BLUE_ABGR_PACKED_INT);
	font.load();
	
	simulationvariablesFont = FontFactory.create(activity.getFontManager(),
			activity.getTextureManager(), 256, 256,
			   Typeface.create(Typeface.DEFAULT, Typeface.BOLD),
			   64f, true, Color.BLACK_ARGB_PACKED_INT);
	simulationvariablesFont.load();
	
	
	final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	//final ITexture mainFontTexture1 = new BitmapTextureAtlas(activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font_score = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "HoboStd.otf", 100f, true, Color.WHITE_ABGR_PACKED_INT, 2, Color.BLACK_ABGR_PACKED_INT);
		font_score.load();
		
		//font_score1 = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture1, activity.getAssets(), "homeworknormal.TTF", 150f, true, Color.WHITE_ABGR_PACKED_INT, 2, Color.BLACK_ABGR_PACKED_INT);
		//font_score1.load();
	
	
	
	
	
}

public void loadGameResources()
{
	loadGameGraphics();
    loadGameFonts();
    loadGameAudio();
    
	loadSimulationGraphics();
    loadSimulationFonts();
    loadSimulationAudio();
}

private void loadGameGraphics() {

	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
    gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
    
    platform1_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform1.png");
   coin_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "target.png");
   platform2_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "platform2.png");
   brickRegion =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "brick.png");
    try 
    {
        this.gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
        this.gameTextureAtlas.load();
    } 
    catch (final TextureAtlasBuilderException e)
    {
        Debug.e(e);
    }
}

private void loadMenuGraphics()
{
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
	menuBtnTextureAtlas= new BuildableBitmapTextureAtlas(engine.getTextureManager(),1500, 1500,TextureOptions.DEFAULT);
	play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuBtnTextureAtlas, activity, "tela_menu_btn_modofases.png");
	simulation_region= BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuBtnTextureAtlas, activity, "menu_bt_simulacao.png");
	options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuBtnTextureAtlas, activity, "menu_bt_configuracao.png");
	lockRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuBtnTextureAtlas, activity, "lock.png");	
	
	
	menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1200, 1200, TextureOptions.BILINEAR);
	menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "tela_menu_fundo.jpg");

	       
	try 
	{
	    this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
	    this.menuTextureAtlas.load();
	    
	    menuBtnTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
	    this.menuBtnTextureAtlas.load();
	} 
	catch (final TextureAtlasBuilderException e)
	{
	        Debug.e(e);
	}
	
	
	levelSelectorTileTextureAtlas= new BitmapTextureAtlas(engine.getTextureManager(),128, 128,TextureOptions.DEFAULT);
	tileTextureRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelSelectorTileTextureAtlas, activity,"tile.png" ,0,0);
	levelSelectorTileTextureAtlas.load();
	
	
	lightTextureAtlas= new BitmapTextureAtlas(engine.getTextureManager(),800, 800, TextureOptions.REPEATING_BILINEAR);
	lightRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(lightTextureAtlas, activity,"tela_menu_lampada.png" ,0,0);
	lightTextureAtlas.load();
	

	
	
}

private void loadMenuAudio()
{
    
}

private void loadSimulationGraphics()
{
	///background
	simulationBGTextureAtlas= new BitmapTextureAtlas(engine.getTextureManager(),128, 128, TextureOptions.REPEATING_BILINEAR);
	repeating_background=BitmapTextureAtlasTextureRegionFactory.createFromAsset(simulationBGTextureAtlas, activity,"fundo_simulacao.png" ,0,0);
	simulationBGTextureAtlas.load();
	
	boardTextureAtlas= new BitmapTextureAtlas(engine.getTextureManager(),1800, 400, TextureOptions.REPEATING_BILINEAR);
	boardRegion=BitmapTextureAtlasTextureRegionFactory.createFromAsset(boardTextureAtlas, activity,"placar.png" ,0,0);
	boardTextureAtlas.load();
	
	
	loadSimulationProjectile();
	loadSimulationGraphicsMenuHUD();
	
	
}
 
private void loadSimulationProjectile() {
	 
	   
	    simulationTextureAtlasProjectile= new BitmapTextureAtlas(engine.getTextureManager(), 32, 32);
	    simulationTextureRegionProjectile = BitmapTextureAtlasTextureRegionFactory.createFromAsset(simulationTextureAtlasProjectile, activity, "bolinha.png",0,0);
	    simulationTextureAtlasProjectile.load();
	
}

private void loadSimulationGraphicsMenuHUD() {
	 ///angle button
	 simulationTextureAtlasAngleButton = new BitmapTextureAtlas(engine.getTextureManager(),700, 350, TextureOptions.DEFAULT);
	 simulationTextureRegionAngleButton=BitmapTextureAtlasTextureRegionFactory.createFromAsset(simulationTextureAtlasAngleButton, activity,"angulo.png" ,0,0);
	 simulationTextureRegionAngleButtonHover=BitmapTextureAtlasTextureRegionFactory.createFromAsset(simulationTextureAtlasAngleButton, activity,"angulo_h.png" ,320,0);
	
	 ///angle button
	 simulationTextureAtlasSpeedButton = new BitmapTextureAtlas(engine.getTextureManager(),700, 350, TextureOptions.DEFAULT);
	 simulationTextureRegionSpeedButton=BitmapTextureAtlasTextureRegionFactory.createFromAsset(simulationTextureAtlasSpeedButton, activity, "vf.png",0,0);
	 simulationTextureRegionSpeedButtonHover=BitmapTextureAtlasTextureRegionFactory.createFromAsset(simulationTextureAtlasSpeedButton, activity, "vf_h.png",320,0);
	 
	 simulationTextureAtlasPlayButton = new BitmapTextureAtlas(engine.getTextureManager(),700, 350, TextureOptions.DEFAULT);
	 simulationTextureRegionPlayButton=BitmapTextureAtlasTextureRegionFactory.createFromAsset(simulationTextureAtlasPlayButton, activity,"play.png" ,0,0);
	 simulationTextureRegionPlayButtonHover=BitmapTextureAtlasTextureRegionFactory.createFromAsset(simulationTextureAtlasPlayButton, activity,"play_h.png" ,320,0);
	 
	 simulationTextureAtlasResetButton = new BitmapTextureAtlas(engine.getTextureManager(),700, 350, TextureOptions.DEFAULT);
	 simulationTextureRegionResetButton=BitmapTextureAtlasTextureRegionFactory.createFromAsset(simulationTextureAtlasResetButton, activity,"reset.png",0,0);
	 simulationTextureRegionResetButtonHover=BitmapTextureAtlasTextureRegionFactory.createFromAsset(simulationTextureAtlasResetButton, activity,"reset_h.png",320,0);
	 
	 
	 simulationTextureAtlasMoveButton = new BitmapTextureAtlas(engine.getTextureManager(),700, 350, TextureOptions.DEFAULT);
	 simulationTextureRegionMoveButton=BitmapTextureAtlasTextureRegionFactory.createFromAsset(simulationTextureAtlasMoveButton, activity,"drag.png",0,0);
	 simulationTextureRegionMoveButtonHover=BitmapTextureAtlasTextureRegionFactory.createFromAsset(simulationTextureAtlasMoveButton, activity,"drag_h.png",320,0);
	 
	 simulationTextureAtlasAngleButton.load();
	 simulationTextureAtlasSpeedButton.load();
	 simulationTextureAtlasPlayButton.load();
	 simulationTextureAtlasResetButton.load();
	 simulationTextureAtlasMoveButton.load();
}

private void loadGameFonts()
{

}

private void loadGameAudio()
{
    
}


public void loadSplashScreen()
{ 
	
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/"); 
	splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 1200, 900,TextureOptions.DEFAULT);
	splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "tela_menu_fundo.jpg", 0, 0);
	splashTextureAtlas.load(); 
	
	


	
}

public void unloadSplashScreen()
{
	splashTextureAtlas.unload();
	splash_region = null;
}

public static void prepareManager(Engine engine, MainActivity activity, BoundCamera camera, VertexBufferObjectManager vbom)
{
    getInstance().engine = engine;
    getInstance().activity = activity;
    getInstance().camera = camera;
    getInstance().vbom = vbom;
    
    

}


public static ResourcesManager getInstance()
{
    return INSTANCE;
}

public void unloadMenuTextures()
{
	//TODO check all textures that should be unloaded
    menuTextureAtlas.unload();
}
    
public void loadMenuTextures()
{
    menuTextureAtlas.load();
}

public void unloadGameTextures()
{
//TODO UNLOAD ALL GAME RESOURCES
	
	gameTextureAtlas.unload();
	simulationBGTextureAtlas.unload();
	font.unload();
	
	
	//game use simulations resources
	simulationvariablesFont.unload();
	simulationTextureAtlasProjectile.unload();
	simulationTextureAtlasAngleButton.unload();
	simulationTextureAtlasSpeedButton.unload();
	simulationTextureAtlasPlayButton.unload();
	simulationTextureAtlasResetButton.unload();
	simulationTextureAtlasMoveButton.unload();
	
	
}

public void unloadSimulationTextures() {
	simulationvariablesFont.unload();
	simulationTextureAtlasProjectile.unload();
	simulationTextureAtlasAngleButton.unload();
	simulationTextureAtlasSpeedButton.unload();
	simulationTextureAtlasPlayButton.unload();
	simulationTextureAtlasResetButton.unload();
	simulationTextureAtlasMoveButton.unload();
	
}	

}
