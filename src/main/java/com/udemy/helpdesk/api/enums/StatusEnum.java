package com.udemy.helpdesk.api.enums;

public enum StatusEnum {
	
	Novo,
	Atribuido,
	Resolvido,
	Aprovado,
	Reprovado,
	Finalizado;
	
	public static StatusEnum getStatus(String status) {
		switch(status) {
			case "Novo":
				return Novo;
			case "Atribuido":
				return Atribuido;
			case "Resolvido":
				return Resolvido;
			case "Aprovado":
				return Aprovado;
			case "Reprovado":
				return Reprovado;
			case "Finalizado":
				return Finalizado;
			default:
				return Novo;
		}
	}
}
