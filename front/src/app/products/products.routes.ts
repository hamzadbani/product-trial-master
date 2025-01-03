import { inject } from "@angular/core";
import { ActivatedRouteSnapshot, Routes } from "@angular/router";
import { ProductListComponent } from "./features/product-list/product-list.component";
import { ContactComponent } from "app/contact/contact.component";

export const PRODUCTS_ROUTES: Routes = [
	{
		path: "list",
		component: ProductListComponent,
	},
	{
		path: "form",
		component: ContactComponent,
	},
	{ path: "**", redirectTo: "list" },
];
