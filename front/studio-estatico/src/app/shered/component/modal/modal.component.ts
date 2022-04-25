import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subject } from 'rxjs';

@Component({
    selector: 'modal-app',
    templateUrl: 'modal.component.html'
})

export class ModalComponent implements OnInit, OnDestroy {

    @Input() modalEvent!:Subject<boolean>;
    @Input() tituloModal!: string;
    @Input() idModal = 'modal';
    $destroy = new Subject<boolean>();

    constructor() { }

    ngOnDestroy(): void {
        this.$destroy.next(true);
        this.$destroy.unsubscribe();
    }

    ngOnInit() { }
}