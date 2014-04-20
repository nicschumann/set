package glfrontend;

public interface Triggerable {
	
	public void trigger(TriggerEvent e);
	
	public static class TriggerEvent {
		
		private Object source;
		
		public TriggerEvent(Object source) {
			this.source = source;
		}
		
		public Object getSource() {
			return source;
		}
		
	}

}
