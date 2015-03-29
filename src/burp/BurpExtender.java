package burp;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JMenuItem;

import util.StringValidator;

public class BurpExtender implements IBurpExtender, IContextMenuFactory {
	// burp objects
	private IBurpExtenderCallbacks callbacks;
	private IExtensionHelpers helpers;
	// current proxy message
	// private IInterceptedProxyMessage message;
	// private boolean messageIsRequest;

	// Extension name
	private static final String EXTENSION_NAME = "Copy for Evidence ver.alpha 25";

	// Context menu text
	public static String CONTEXT_COPY_ALL = "Copy for Evidence (All text)";
	public static String CONTEXT_COPY_HEADER = "Copy for Evidence (HTTP Header)";
	public static String CONTEXT_COPY_BODY = "Copy for Evidence (Body)";
	public static String CONTEXT_COPY_UP_TO_HEAD_ELEMENT = "Copy for Evidence (up to HEAD element)";
	public static String CONTEXT_COPY_SELECTED = "Copy for Evidence (Selected text)";

	// obtain our output and error streams
	private static PrintWriter stdout;
	private static PrintWriter stderr;

	//
	// implement IBurpExtender
	//

	@Override
	public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
		// keep a reference to our callbacks object
		this.callbacks = callbacks;

		// obtain an extension helpers object
		helpers = callbacks.getHelpers();

		// set our extension name
		callbacks.setExtensionName(EXTENSION_NAME);

		// register ourselves as a new context menu factory
		callbacks.registerContextMenuFactory(this);

		// register ourselves as a new proxy listener
		// callbacks.registerProxyListener(this);

		// obtain our output and error streams
		stdout = new PrintWriter(callbacks.getStdout(), true);
		stderr = new PrintWriter(callbacks.getStderr(), true);
	}

	//
	// implement IContextMenuFactory
	//
	@Override
	public List<JMenuItem> createMenuItems(IContextMenuInvocation invocation) {

		// store argument
		final IContextMenuInvocation in = invocation;
		final IHttpRequestResponse messages[] = in.getSelectedMessages();
		final byte context = in.getInvocationContext();

		// menuItems
		List<JMenuItem> menuItems = new LinkedList<JMenuItem>();

		// menu item to add
		JMenuItem menuItemCopyAll = new JMenuItem(CONTEXT_COPY_ALL);
		JMenuItem menuItemCopyHeader = new JMenuItem(CONTEXT_COPY_HEADER);
		JMenuItem menuItemCopySelected = new JMenuItem(CONTEXT_COPY_SELECTED);

		// define event handler
		menuItemCopyAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// write a message to our output stream
				stdout.println("==============================================================================");
				stdout.println("Selected: " + e.getActionCommand());
				stdout.println("Context: " + context);
				stdout.println("Message count: " + messages.length);

				// if the selected menu is
				if (e.getActionCommand().equals(CONTEXT_COPY_ALL)) {

					// processing if any messages are selected
					if (messages != null && messages.length > 0) {
						IHttpRequestResponse message = messages[0];

						if (context == IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_REQUEST
								|| context == IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_REQUEST) {
							// if request
							setClipboard(message.getRequest());
						} else if (context == IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_RESPONSE
								|| context == IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_RESPONSE) {
							// if response
							setClipboard(message.getResponse());
						} else if (context == IContextMenuInvocation.CONTEXT_PROXY_HISTORY) {
							// if history selected, copy request of first item
							setClipboard(message.getRequest());
						} else {
							// if no item is selected, only write log
							stdout.println("no item selected.");
						}

						// write message log
						// for (IHttpRequestResponse m : messages) {
						// stdout.println("Request : ");
						// stdout.println(new String(m.getRequest()));
						// stdout.println("Response : ");
						// stdout.println(new String(m.getResponse()));
						// }
					}
				}

				stdout.println("==============================================================================");
			}
		});

		// define event handler
		menuItemCopyHeader.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// write a message to our output stream
				stdout.println("==============================================================================");
				stdout.println("Selected: " + e.getActionCommand());
				stdout.println("Context: " + context);
				stdout.println("Message count: " + messages.length);

				// if the selected menu is
				if (e.getActionCommand().equals(CONTEXT_COPY_HEADER)) {

					// processing if any messages are selected
					if (messages != null && messages.length > 0) {
						IHttpRequestResponse message = messages[0];

						if (context == IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_REQUEST
								|| context == IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_REQUEST) {
							// if request
							byte[] bytes = message.getRequest();
							IRequestInfo request = helpers
									.analyzeRequest(bytes);
							try {
								byte[] subbytes = Arrays.copyOfRange(bytes, 0,
										request.getBodyOffset());
								setClipboard(subbytes);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						} else if (context == IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_RESPONSE
								|| context == IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_RESPONSE) {
							// if response
							byte[] bytes = message.getResponse();
							IResponseInfo response = helpers
									.analyzeResponse(bytes);
							try {
								byte[] subbytes = Arrays.copyOfRange(bytes, 0,
										response.getBodyOffset());
								setClipboard(subbytes);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						} else if (context == IContextMenuInvocation.CONTEXT_PROXY_HISTORY) {
							// if history selected, copy request of first item
							byte[] bytes = message.getRequest();
							IRequestInfo request = helpers
									.analyzeRequest(bytes);
							try {
								byte[] subbytes = Arrays.copyOfRange(bytes, 0,
										request.getBodyOffset());
								setClipboard(subbytes);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						} else {
							// if no item is selected, only write log
							stdout.println("no item selected.");
						}

						// write message log
						// for (IHttpRequestResponse m : messages) {
						// stdout.println("Request : ");
						// stdout.println(new String(m.getRequest()));
						// stdout.println("Response : ");
						// stdout.println(new String(m.getResponse()));
						// }
					}
				}

				stdout.println("==============================================================================");
			}
		});

		// define event handler
		menuItemCopySelected.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// write a message to our output stream
				stdout.println("==============================================================================");
				stdout.println("Selected: " + e.getActionCommand());
				stdout.println("Context: " + context);
				stdout.println("Message count: " + messages.length);

				// if the selected menu is
				if (e.getActionCommand().equals(CONTEXT_COPY_SELECTED)) {

					// processing if any messages are selected
					if (messages != null && messages.length > 0) {
						IHttpRequestResponse message = messages[0];

						int[] selectionBounds = in.getSelectionBounds();
						if (selectionBounds != null
								&& selectionBounds.length == 2
								&& selectionBounds[0] < selectionBounds[1]) {

							// selection offsets
							int from = selectionBounds[0];
							int to = selectionBounds[1];

							if (context == IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_REQUEST
									|| context == IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_REQUEST) {
								// if request
								byte[] bytes = message.getRequest();
								byte[] selectionBytes = Arrays.copyOfRange(bytes, from, to);
								setClipboard(selectionBytes);
							} else if (context == IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_RESPONSE
									|| context == IContextMenuInvocation.CONTEXT_MESSAGE_VIEWER_RESPONSE) {
								// if response
								byte[] bytes = message.getResponse();
								byte[] selectionBytes = Arrays.copyOfRange(bytes, from, to);
								setClipboard(selectionBytes);
							} else if (context == IContextMenuInvocation.CONTEXT_PROXY_HISTORY) {
								// if history selected, copy request of first
								// item
								byte[] bytes = message.getRequest();
								byte[] selectionBytes = Arrays.copyOfRange(bytes, from, to);
								setClipboard(selectionBytes);
							} else {
								// if no item is selected, only write log
								stdout.println("no item selected.");
							}
						}
					}
				}

				stdout.println("==============================================================================");
			}
		});

		menuItems.add(menuItemCopyAll);
		menuItems.add(menuItemCopyHeader);
		menuItems.add(menuItemCopySelected);

		return (menuItems);
	}

	//
	// implement IProxyListener
	//
	// @Override
	// public void processProxyMessage(boolean messageIsRequest,
	// IInterceptedProxyMessage message) {
	// // set arguments to fields
	// this.messageIsRequest = messageIsRequest;
	// this.message = message;
	// }

	//
	// Copy text to clipboard
	//
	public static void setClipboard(byte[] bytes) {
		try {
			// detect character encoding and set clipboard
			if (StringValidator.isSJIS(bytes)) {
				setClipboard(new String(bytes, "SJIS"));
				stdout.println("charset: SJIS");
				stdout.println("copied text: ");
				stdout.println(new String(bytes, "SJIS"));
			} else if (StringValidator.isWindows31j(bytes)) {
				setClipboard(new String(bytes, "Windows-31j"));
				stdout.println("charset: Windows-31j");
				stdout.println("copied text: ");
				stdout.println(new String(bytes, "Windows-31j"));
			} else if (StringValidator.isEUC(bytes)) {
				setClipboard(new String(bytes, "euc-jp"));
				stdout.println("charset: euc-jp");
				stdout.println("copied text: ");
				stdout.println(new String(bytes, "euc-jp"));
			} else if (StringValidator.isUTF8(bytes)) {
				setClipboard(new String(bytes, "UTF-8"));
				stdout.println("charset: UTF-8");
				stdout.println("copied text: ");
				stdout.println(new String(bytes, "UTF-8"));
			} else {
				setClipboard(new String(bytes, "SJIS"));
				stdout.println("charset: unknown");
				stdout.println("copied text: ");
				stdout.println(new String(bytes, "SJIS"));
			}
		} catch (Exception e) {
			stderr.println(e.getMessage());
		}
	}

	//
	// Copy text to clipboard
	//
	public static void setClipboard(String text) {
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		StringSelection selection = new StringSelection(text);
		clipboard.setContents(selection, selection);
	}
}
