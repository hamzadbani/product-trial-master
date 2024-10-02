import { CommonModule } from "@angular/common";
import { Component, OnInit, inject, signal } from "@angular/core";
import { Product } from "app/products/data-access/product.model";
import { ProductsService } from "app/products/data-access/products.service";
import { ProductFormComponent } from "app/products/ui/product-form/product-form.component";
import { ButtonModule } from "primeng/button";
import { CardModule } from "primeng/card";
import { DataViewModule } from 'primeng/dataview';
import { DialogModule } from 'primeng/dialog';

const emptyProduct: Product = {
  id: 0,
  code: "",
  name: "",
  description: "",
  image: "",
  category: "",
  price: 0,
  quantity: 0,
  internalReference: "",
  shellId: 0,
  inventoryStatus: "INSTOCK",
  rating: 0,
  createdAt: 0,
  updatedAt: 0,
};

@Component({
  selector: "app-product-list",
  templateUrl: "./product-list.component.html",
  styleUrls: ["./product-list.component.scss"],
  standalone: true,
  imports: [DataViewModule, CardModule, ButtonModule, DialogModule, ProductFormComponent, CommonModule],
})
export class ProductListComponent implements OnInit {
  private readonly productsService = inject(ProductsService);

  public readonly products = this.productsService.products;

  public isDialogVisible = false;
  public isCreation = false;
  public readonly editedProduct = signal<Product>(emptyProduct);
  public readonly cart = signal<Product[]>([]);
  public isCartVisible = false;  
  public readonly cartBadgeCount = signal<number>(0);

  ngOnInit() {
    this.productsService.get().subscribe();
  }

  public onCreate() {
    this.isCreation = true;
    this.isDialogVisible = true;
    this.editedProduct.set(emptyProduct);
  }

  public onUpdate(product: Product) {
    this.isCreation = false;
    this.isDialogVisible = true;
    this.editedProduct.set(product);
  }

  public onDelete(product: Product) {
    this.productsService.delete(product.id).subscribe();
  }

  public onSave(product: Product) {
    if (this.isCreation) {
      this.productsService.create(product).subscribe();
    } else {
      this.productsService.update(product).subscribe();
    }
    this.closeDialog();
  }
  public onCancel() {
    this.closeDialog();
  }

  private closeDialog() {
    this.isDialogVisible = false;
  }

  public onAddToCart(product: Product) {
    debugger
    const existingProduct = this.cart().find(p => p.id === product.id);
    if (!existingProduct) {
      this.cart.update(cart => [...cart, { ...product, quantity: 1 }]);
    } else {
      this.cart.update(cart =>
        cart.map(p => (p.id === product.id ? { ...p, quantity: p.quantity + 1 } : p))
      );
    }
    this.updateCartBadgeCount();
  }
  
  private updateCartBadgeCount() {
    const cartLength = this.cart().length;
    this.cartBadgeCount.set(cartLength);
  }

  public onRemoveFromCart(product: Product) {
    this.cart.update(cart => cart.filter(p => p.id !== product.id));
    this.updateCartBadgeCount();
  }

  public onViewCart() {
    console.log('Viewing cart:', this.cart());
    this.isCartVisible = true;
  }
}