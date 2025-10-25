package exercises.scoped_values.sv_01_basic_api.base;

/*
 * Diese Aufgabe demonstriert das grundlegende API von Scoped Values: where() und run().
 * 
 * Es geht dabei um das Binden von Werten innerhalb von Scopes und den Zugriff auf diese Werte.
 * Alles findet im Main Thread statt(!).
 * 
 * A) Binding und Scope
 * Benutze eine Variable vom Typ ScopedValue. Weise ihr mithilfe der Methode where() einen Wert innerhalb eines Scope zu. 
 * Führe mit run() Ein Lambda in diesem Scope aus. Greife aus dem Lambda auf den Wert zu und schreibe ihn auf die Console.
 * 
 * B) Chainig
 * Instanziiere mehrere Variablen vom Typ Scoped Value. Benutze mehrere verkettete where() Aufrufe um ihnen Werte zuzusweisen,
 * die im selben Scope gueltig sind. Starte mit run() ein Lambda, das auf alle diese Werte zugreifen kann, und schreibe sie auf die 
 * Console.
 * 
 * C) Rebinding:
 * Eroeffne innerhalb eines Scopes einen Sub-Scope.
 * Binde im Sub-Scope einen neuen Wert an den Scoped-Value. 
 * Zeige, dass innerhalb des Sub-Scopes der neue Wert gitl, und dass nach dem Verlassen des Sub-Scopes wieder der alte Wert gueltig ist.  
 * 
 */
public class ScopedValueBasicAPI_BASE {
	
	
	// TODO final static ScopedValue<String> SCOPED_VALUE = ScopedValue.newInstance();	
	
	// TODO
	//final static ScopedValue<String> SCOPED_VALUE_2 = ScopedValue.newInstance();
	//final static ScopedValue<String> SCOPED_VALUE_3 = ScopedValue.newInstance();
	
	public static void main(String[] args) throws InterruptedException {
	
		ScopedValueBasicAPI_BASE instance = new ScopedValueBasicAPI_BASE();
		
		// A)
		instance.scopedValueBasics();
		
		// B
		instance.scopedValueChain();
		
	}
	
	/*
	 * A) Grundlegendes Scoped-Value API: Einen Wert binden und einen Scope eroeffnen
	 */
	void scopedValueBasics() throws InterruptedException {
		
		System.out.println(" A) scopedValueBasics() method running in: " + Thread.currentThread());
			
		// TODO Scoped Value binden mit where() und Scope definieren mit run(); Bemerkung: run() startet keinen neuen Thread
		
		//ScopedValue.where(SCOPED_VALUE, "Hallo").run(() -> {	//...	});
			
		
		// TODO Innerhalb des Scopes ausfuehren
		/*
		  	System.out.println("scopedValueBasics() method running in: " + Thread.currentThread());
			String valueinScope = SCOPED_VALUE.get();
			System.out.println("Value in Scope: : " + valueinScope);

		 */
	}
	
	/*
	 * B) Mehrere Scoped-Values sind in einem Scope gueltig
	 */
	void scopedValueChain() throws InterruptedException {
		
		System.out.println("B): scopedValueTest() outer method running in: " + Thread.currentThread());
			
		
		// TODO Zuweisungen mit where().where() verketten 
		//  scopedValue.where(SCOPED_VALUE, "Hallo").where(SCOPED_VALUE_2, " Lieber ").where(SCOPED_VALUE_3, "Nachbar")
		
		// TODO mit run() einen Scope eroeffenen: // .run(() -> {});
		
		
		// TODO innerhalb des neuen Scopes auf alle drei ScopedValue Instanzen zugreifen
		/*
		System.out.println("scopedValueChain() method running in: " + Thread.currentThread());
		String concatChain = SCOPED_VALUE.get() + SCOPED_VALUE_2.get() + SCOPED_VALUE_3.get();
		System.out.println("Die drei Werte sind: : " + concatChain);
		 */
			
	}
	
	/*
	 * C) Rebinding
	 */
	void scopedValueRebinding() throws InterruptedException {
		

		// TODO:  Scoped Value binden mit where() und Scope definieren mit run(): // ScopedValue.where(SCOPED_VALUE, "Aeusserer Wert").run(() -> { //... });

			// TODO: Zugriff auf den Wert im aeusseren Scope: // System.out.println(" Value Thread outer: " + SCOPED_VALUE.get());
			
			// TODO: Rebinding: // ScopedValue.where(SCOPED_VALUE, "Innerer Wert").run(() -> {
				// TODO: Zugriff auf den Wert im inneren Scope System.out.println(" Value Thread inner: " + SCOPED_VALUE.get()); });
			
			// TODO: Zugriff auf den Wert im aeusseren Scope nachdem das Rebinding stattgefunden hat: // System.out.println(" Value Thread outer after rebind: " + SCOPED_VALUE.get());
			// (liefert wieder den aeusseren Wert)
				
		
	}

}
