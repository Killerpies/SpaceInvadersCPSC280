import java.awt.event.WindowEvent;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.google.common.truth.Truth;

import edu.cnu.cs.gooey.Gooey;
import edu.cnu.cs.gooey.GooeyDialog;
import edu.cnu.cs.gooey.GooeyFrame;

class SpaceInvadersTest {
	private static final Class<?> CLASS = SpaceInvaders.class;

	@BeforeAll
	static void preLoad() {
		new SpaceInvaders().dispose();
	}

	private static void runMain() {
		SpaceInvaders.main( new String[] {} );
	}

	@Nested
	class NonFunctionalTests {
		void testClassIsJFrameAndNoFieldsAreJFrame() {
			BiConsumer<Class<?>,Class<?>> isSubclassOf = (clazz,expected) -> {
				var actual = clazz.getSuperclass();
				Truth.assertWithMessage( String.format( "'%s' is not a subclass of '%s'; found '%s'", clazz.getSimpleName(), expected.getSimpleName(), actual.getSimpleName() ))
	                 .that             ( actual )
	                 .isEqualTo        ( expected );
			};
			Consumer<Class<?>> fieldsNotJFrame = c -> Arrays.stream( c.getDeclaredFields() ).filter( f->!f.isSynthetic() ).forEach( f->{
				var type = f.getType();
				var name = f.getName();
				Truth.assertWithMessage( String.format("field '%s.%s' can't be of type JFrame", c.getSimpleName(), name ))
				     .that             ( type )
				     .isNotEqualTo     ( JFrame.class );
			});

			isSubclassOf   .accept( CLASS, JFrame.class );
			fieldsNotJFrame.accept( CLASS );
		}
		void testFieldsArePrivateAndNonStatic() {
			Consumer<Class<?>> fieldsPrivate = c -> Arrays.stream( c.getDeclaredFields() ).filter( f->!f.isSynthetic() ).forEach( f->{
				var mod  = f.getModifiers();
				var name = f.getName(); 
				Truth.assertWithMessage( String.format("field '%s' is not private", name ))
				     .that             ( Modifier.isPrivate( mod ))
				     .isTrue();
			});
			Consumer<Class<?>> fieldsNotStatic = c -> Arrays.stream( c.getDeclaredFields() ).filter( f->!f.isSynthetic() ).forEach( f->{
				var mod  = f.getModifiers();
				var name = f.getName(); 
				Truth.assertWithMessage( String.format("field '%s' is static", name ))
				     .that             ( Modifier.isStatic( mod ))
				     .isFalse();
			});
			
			fieldsPrivate  .accept( CLASS );
			fieldsNotStatic.accept( CLASS );
		}
		@Test
		void testWindowIsClassInstanceWithTitle() {
			Gooey.capture(new GooeyFrame() {
				@Override
				public void invoke() {
					runMain();
				}
				@Override
				public void test(JFrame frame) {
					// JFrame
					Truth.assertWithMessage( String.format( "Frame is not instance of '%s' (did you create a new JFrame in a local variable?)", CLASS.getSimpleName() ))
					     .that             ( frame.getClass() )
					     .isAssignableTo   ( CLASS );
					// Title
					var title = frame.getTitle();
					Truth.assertThat( title ).isNotNull();
					Truth.assertThat( title ).isNotEmpty();
				}
			});
		}
		@Test
		void testWindowDoesNothingOnClose() {
			Gooey.capture(new GooeyFrame() {
				@Override
				public void invoke() {
					runMain();
				}
				@Override
				public void test(JFrame frame) {
					var op = frame.getDefaultCloseOperation();
					Truth.assertWithMessage( "frame should do nothing on close" )
					     .that             ( op )
					     .isEqualTo        ( JFrame.DO_NOTHING_ON_CLOSE );
					
					Truth.assertWithMessage( "frame should have a window listener" ).that( frame.getWindowListeners() ).isNotEmpty();					
				}
			});
		}
		@Test
		void testWindowIsPackedAndCentered() {
			Gooey.capture(new GooeyFrame() {
				@Override
				public void invoke() {
					runMain();
				}
				@Override
				public void test(JFrame frame) {
					var actual   = frame.getBounds();
					frame.pack();
					frame.setLocationRelativeTo( null );
					var expected = frame.getBounds();
					Truth.assertWithMessage( "window not packed" )
					     .that             ( actual  .getSize() )
					     .isEqualTo        ( expected.getSize() );
					Truth.assertWithMessage( "window not centered on screen" )
				         .that             ( actual  .getLocation() )
				         .isEqualTo        ( expected.getLocation() );
				}
			});
		}
		@Test
		void testMenusExistAreEnabledAndHaveListeners() {
			Gooey.capture( new GooeyFrame() {
				@Override
				public void invoke() {
					runMain();
				}
				@Override
				public void test(JFrame frame) {
					JMenuBar  menubar = Gooey.getMenuBar( frame );
					
					JMenu     game    = Gooey.getMenu( menubar, "Game" );
					JMenuItem newGame = Gooey.getMenu( game, "New Game" ); 
					JMenuItem pause   = Gooey.getMenu( game, "Pause" ); 
					JMenuItem resume  = Gooey.getMenu( game, "Resume" ); 
					JMenuItem quit    = Gooey.getMenu( game, "Quit" ); 

					JMenu     help    = Gooey.getMenu( menubar, "Help" );
					JMenuItem about   = Gooey.getMenu( help, "About..." );
					
					Truth.assertWithMessage( "'New Game' should have an action listener" ).that( newGame.getActionListeners() ).isNotEmpty();
					Truth.assertWithMessage( "'Pause' should have an action listener"    ).that( pause  .getActionListeners() ).isNotEmpty();
					Truth.assertWithMessage( "'Resume' should have an action listener"   ).that( resume .getActionListeners() ).isNotEmpty();
					Truth.assertWithMessage( "'Quit' should have an action listener"     ).that( quit   .getActionListeners() ).isNotEmpty();
					Truth.assertWithMessage( "'About...' should have an action listener" ).that( about  .getActionListeners() ).isNotEmpty();
					
					Truth.assertThat( newGame.isEnabled() ).isTrue();
					Truth.assertThat( pause  .isEnabled() ).isFalse();
					Truth.assertThat( resume .isEnabled() ).isFalse();
					Truth.assertThat( quit   .isEnabled() ).isTrue();
					Truth.assertThat( about  .isEnabled() ).isTrue();
				}
			});
		}
		@Test
		void testQuitMenuDisplaysConfirmDialog() {
			Gooey.capture(new GooeyFrame() {
				@Override
				public void invoke() {
					runMain();
				}
				@Override
				public void test(JFrame frame) {
					JMenuBar  menubar = Gooey.getMenuBar( frame );
					JMenu     game    = Gooey.getMenu( menubar, "Game" );
					JMenuItem quit    = Gooey.getMenu( game,    "Quit" );

					Gooey.capture(new GooeyDialog() {
						@Override
						public void invoke() {
							quit.doClick();
						}
						@Override
						public void test(JDialog dialog) {
							var parent = dialog.getParent();
							Truth.assertWithMessage( "'Quit' dialog is centered on the screen (it should be centered on the frame" )
						         .that( parent ).isNotNull();
							Truth.assertWithMessage( "'Quit' dialog should be centered on the frame (but it is not)" )
						         .that( parent ).isSameInstanceAs( frame );
							
							Gooey.getButton( dialog, "Yes" );
							Gooey.getButton( dialog, "No"  ).doClick();
						}
					});
				}
			});
		}
		@Test
		void testNewGameMenuDisplaysConfirmDialog() {
			Gooey.capture(new GooeyFrame() {
				@Override
				public void invoke() {
					runMain();
				}
				@Override
				public void test(JFrame frame) {
					JMenuBar  menubar = Gooey.getMenuBar( frame );
					JMenu     game    = Gooey.getMenu( menubar, "Game" );
					JMenuItem newGame = Gooey.getMenu( game,    "New Game" );

					Gooey.capture(new GooeyDialog() {
						@Override
						public void invoke() {
							newGame.doClick();
						}
						@Override
						public void test(JDialog dialog) {
							var parent = dialog.getParent();
							Truth.assertWithMessage( "'New Game' dialog is centered on the screen (it should be centered on the frame" )
						         .that( parent ).isNotNull();
							Truth.assertWithMessage( "'New Game' dialog should be centered on the frame (but it is not)" )
						         .that( parent ).isSameInstanceAs( frame );

							Gooey.getButton( dialog, "Yes" );
							Gooey.getButton( dialog, "No"  ).doClick();
						}
					});
				}
			});
		}
		@Test
		void testAboutMenuDisplaysMessageDialog() {
			Gooey.capture(new GooeyFrame() {
				@Override
				public void invoke() {
					runMain();
				}
				@Override
				public void test(JFrame frame) {
					JMenuBar  menubar = Gooey.getMenuBar( frame );
					JMenu     help    = Gooey.getMenu( menubar, "Help" );
					JMenuItem about   = Gooey.getMenu( help, "About..." );

					Gooey.capture(new GooeyDialog() {
						@Override
						public void invoke() {
							about.doClick();
						}
						@Override
						public void test(JDialog dialog) {
							var parent = dialog.getParent();
							Truth.assertWithMessage( "'About' dialog is centered on the screen (it should be centered on the frame" )
						         .that( parent ).isNotNull();
							Truth.assertWithMessage( "'About' dialog should be centered on the frame (but it is not)" )
						         .that( parent ).isSameInstanceAs( frame );

							Gooey.getButton( dialog, "OK" ).doClick();
						}
					});
				}
			});
		}
		@Test
		void testCloseIconShowsConfirmDialog() {
			Gooey.capture(new GooeyFrame() {
				@Override
				public void invoke() {
					runMain();
				}
				@Override
				public void test(JFrame frame) {
					Gooey.capture(new GooeyDialog() {
						@Override
						public void invoke() {
							frame.dispatchEvent(new WindowEvent( frame, WindowEvent.WINDOW_CLOSING ));
						}
						@Override
						public void test(JDialog dialog) {
							var parent = dialog.getParent();
							Truth.assertWithMessage( "'Quit' dialog is centered on the screen (it should be centered on the frame" )
						         .that( parent ).isNotNull();
							Truth.assertWithMessage( "'Quit' dialog should be centered on the frame (but it is not)" )
						         .that( parent ).isSameInstanceAs( frame );
							
							Gooey.getButton( dialog, "Yes" );
							Gooey.getButton( dialog, "No"  ).doClick();
						}
					});
				}
			});
		}
	}

	@Nested
	class FunctionalTests {
		@Test
		void testCloseIconClosesWindowUsingDialog() {
			Gooey.capture(new GooeyFrame() {
				@Override
				public void invoke() {
					runMain();
				}
				@Override
				public void test(JFrame frame) {
					Truth.assertWithMessage( "frame should be visible when program starts" )
						 .that  ( frame.isVisible() )
						 .isTrue();
					
					// displaying dialog & choosing "no" to quit
					Gooey.capture(new GooeyDialog() {
						@Override
						public void invoke() {
							frame.dispatchEvent(new WindowEvent( frame, WindowEvent.WINDOW_CLOSING ));
						}
						@Override
						public void test(JDialog dialog) {
							Gooey.getButton( dialog, "No" ).doClick();
						}
					});

					Truth.assertWithMessage( "Frame should remain visible after user selects 'No'" )
						 .that  ( frame.isVisible() )
						 .isTrue();

					
					// displaying dialog & choosing "yes" to quit
					Gooey.capture(new GooeyDialog() {
						@Override
						public void invoke() {
							frame.dispatchEvent(new WindowEvent( frame, WindowEvent.WINDOW_CLOSING ));
						}
						@Override
						public void test(JDialog dialog) {
							Gooey.getButton( dialog, "Yes" ).doClick();
						}
					});
					Truth.assertWithMessage( "Frame should close after user selects 'Yes'" )
						 .that   ( frame.isVisible() )
						 .isFalse();
				}
			});
		}
		@Test
		void testQuitMenuClosesWindowUsingDialog() {
			Gooey.capture(new GooeyFrame() {
				@Override
				public void invoke() {
					runMain();
				}
				@Override
				public void test(JFrame frame) {
					Truth.assertWithMessage( "frame should be visible when program starts" )
						 .that  ( frame.isVisible() )
						 .isTrue();
					
					JMenuBar  menubar = Gooey.getMenuBar( frame );
					JMenu     game    = Gooey.getMenu( menubar, "Game" );
					JMenuItem quit    = Gooey.getMenu( game, "Quit" );

					// displaying dialog & choosing "no" to quit
					Gooey.capture(new GooeyDialog() {
						@Override
						public void invoke() {
							quit.doClick();
						}
						@Override
						public void test(JDialog dialog) {
							Gooey.getButton( dialog, "No" ).doClick();
						}
					});
					Truth.assertWithMessage( "Frame should remain visible after user selects 'No'" )
						 .that  ( frame.isVisible() )
						 .isTrue();

					
					// displaying dialog & choosing "yes" to quit
					Gooey.capture(new GooeyDialog() {
						@Override
						public void invoke() {
							quit.doClick();
						}
						@Override
						public void test(JDialog dialog) {
							Gooey.getButton( dialog, "Yes" ).doClick();
						}
					});
					Truth.assertWithMessage( "Frame should close after user selects 'Yes'" )
						 .that   ( frame.isVisible() )
						 .isFalse();
				}
			});
		}
		@Test
		void testNewGameEnablesPauseMenu() {
			Gooey.capture(new GooeyFrame() {
				@Override
				public void invoke() {
					runMain();
				}
				@Override
				public void test(JFrame frame) {
					JMenuBar  menubar = Gooey.getMenuBar( frame );
					JMenu     game    = Gooey.getMenu( menubar, "Game" );
					JMenuItem newGame = Gooey.getMenu( game, "New Game" );
					JMenuItem pause   = Gooey.getMenu( game, "Pause" ); 
					JMenuItem resume  = Gooey.getMenu( game, "Resume" ); 

					Truth.assertThat( pause  .isEnabled() ).isFalse();
					Truth.assertThat( resume .isEnabled() ).isFalse();

					// displaying dialog & choosing "no" to new game
					Gooey.capture(new GooeyDialog() {
						@Override
						public void invoke() {
							newGame.doClick();
						}
						@Override
						public void test(JDialog dialog) {
							Gooey.getButton( dialog, "No" ).doClick();
						}
					});

					Truth.assertThat( pause  .isEnabled() ).isFalse();
					Truth.assertThat( resume .isEnabled() ).isFalse();

					// displaying dialog & choosing "yes" to new game
					Gooey.capture(new GooeyDialog() {
						@Override
						public void invoke() {
							newGame.doClick();
						}
						@Override
						public void test(JDialog dialog) {
							Gooey.getButton( dialog, "Yes" ).doClick();
						}
					});

					Truth.assertThat( pause  .isEnabled() ).isTrue();
					Truth.assertThat( resume .isEnabled() ).isFalse();
				}
			});
		}
		@Test
		void testPauseMenuEnablesResumeMenuAndViceversa() {
			Gooey.capture(new GooeyFrame() {
				@Override
				public void invoke() {
					runMain();
				}
				@Override
				public void test(JFrame frame) {
					JMenuBar  menubar = Gooey.getMenuBar( frame );
					JMenu     game    = Gooey.getMenu( menubar, "Game" );
					JMenuItem newGame = Gooey.getMenu( game, "New Game" );
					JMenuItem pause   = Gooey.getMenu( game, "Pause" ); 
					JMenuItem resume  = Gooey.getMenu( game, "Resume" ); 

					Truth.assertThat( pause  .isEnabled() ).isFalse();
					Truth.assertThat( resume .isEnabled() ).isFalse();

					// displaying dialog & choosing "yes" to new game
					Gooey.capture(new GooeyDialog() {
						@Override
						public void invoke() {
							newGame.doClick();
						}
						@Override
						public void test(JDialog dialog) {
							Gooey.getButton( dialog, "Yes" ).doClick();
						}
					});

					Truth.assertThat( pause  .isEnabled() ).isTrue();
					Truth.assertThat( resume .isEnabled() ).isFalse();

					pause.doClick(); // it disables pause & enables resume
					
					Truth.assertThat( pause  .isEnabled() ).isFalse();
					Truth.assertThat( resume .isEnabled() ).isTrue();

					resume.doClick(); // it disables resume & enables pause

					Truth.assertThat( pause  .isEnabled() ).isTrue();
					Truth.assertThat( resume .isEnabled() ).isFalse();
				}
			});
		}
	}
}
