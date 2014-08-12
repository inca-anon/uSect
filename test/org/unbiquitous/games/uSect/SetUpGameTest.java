package org.unbiquitous.games.uSect;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.fest.assertions.data.MapEntry;
import org.junit.Before;
import org.junit.Test;
import org.unbiquitous.games.uSect.environment.Environment;
import org.unbiquitous.uImpala.engine.asset.AssetManager;
import org.unbiquitous.uImpala.engine.core.GameComponents;
import org.unbiquitous.uImpala.engine.core.GameSettings;
import org.unbiquitous.uImpala.engine.io.MouseManager;
import org.unbiquitous.uImpala.engine.io.Screen;
import org.unbiquitous.uImpala.engine.io.ScreenManager;
import org.unbiquitous.uImpala.engine.time.DeltaTime;
import org.unbiquitous.uos.core.adaptabitilyEngine.Gateway;
import org.unbiquitous.uos.core.messageEngine.messages.Response;

public class SetUpGameTest {
	private DeltaTime timer;
	private ScreenManager screens;
	private Screen screen;
	private GameSettings settings;

	@Before public void setUp(){
		TestUtils.setUpEnvironment();
		
		timer = mock(DeltaTime.class);
		GameComponents.put(DeltaTime.class, timer);
		
		screens = mock(ScreenManager.class);
		screen = mock(Screen.class);
		when(screens.create()).thenReturn(screen);
		GameComponents.put(ScreenManager.class, screens);
		
		settings = GameComponents.get(GameSettings.class);
	}
	
	@Test public void deltaTimeIs30(){
		new StartScene();
		verify(timer).setUPS(30);
	}
	
	@Test public void setUpOpensAScreen(){
		new StartScene();
		verify(screen).open();
		assertThat(GameComponents.get(Screen.class)).isEqualTo(screen);
		assertThat(GameComponents.get(AssetManager.class)).isNotNull();
	}
	
	@Test public void setUpOpensAScreenWithSpecifiedWidthIfRequested(){
		settings.put("usect.width", 800);
		settings.put("usect.height", 600);
		new StartScene();
		verify(screen).open("uSect",800,600,false,null);
	}
	
	@Test public void populatesAnEnvironmentWithSects(){
		StartScene scene = new StartScene();
		scene.update();
		assertThat(scene.getChildren()).hasSize(1);
		Environment e = (Environment) scene.getChildren().get(0);
		assertThat(e.sects()).isNotEmpty();
	}
	
	@Test public void noPlayerMustBeSetByDefault(){
		StartScene scene = new StartScene();
		scene.update();
		Environment e = (Environment) scene.getChildren().get(0);
		assertThat(e.players()).isEmpty();
	}
	
	@Test public void setPlayerIfInformed(){
//		settings.put("usect.player.name","John");
		settings.put("usect.player.id",UUID.randomUUID().toString());
		GameComponents.put(MouseManager.class, new MouseManager());
		StartScene scene = new StartScene();
		scene.update();
		Environment e = (Environment) scene.getChildren().get(0);
		assertThat(e.players()).hasSize(1);
	}
	
	
	@Test public void deploysASectDriverToManageInteractions(){
		Gateway gateway = mock(Gateway.class);
		GameComponents.put(Gateway.class, gateway);
		new StartScene();
		verify(gateway).addDriver(any(USectDriver.class));
	}
}
