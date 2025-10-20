import {App, SweetAlert} from "/common/js/app.js";
import {FormHandler} from "/common/js/form.js";
import {RequestHeader} from "/common/js/constant.js";

export const ResourceCreation = (function () {
    const module = {
        fileUploadingInputSelector: $('#file-uploading-input'),
        resourceCreateDescriptionSelector: $('#resource-create-description'),
        createResourceModalSelector: $('#create-resource-modal'),
        createResourceFormSelector: $('#create-resource-form'),
        categoryIdsSelector: $('#create-resource-form .category-select'),
        saveResourceButton: $('#create-resource-form .btn-save')
    };

    module.init = () => {
        handleSaveResourceButton();
    }

    const getResourceRequestParams = () => {
        const formData = new FormData();
        const files = module.fileUploadingInputSelector.prop('files');
        const fileMetadata = [];
        for (const file of files) {
            const uuid = crypto.randomUUID();
            const fileName = file.name;
            const fileExtension = fileName.substring(fileName.lastIndexOf('.'));
            formData.append('files', file, uuid + fileExtension);
            fileMetadata.push({
                resourceId: uuid,
                title: fileName,
                mimeType: file.type,
                fileSize: file.size
            });
        }

        const metadata = {
            files: fileMetadata,
            resourceType: $('input[name="resourceType"]:checked').val(),
            description: module.resourceCreateDescriptionSelector.val(),
            categoryIds: module.categoryIdsSelector.selectpicker('val')
        };

        const metadataBlob = new Blob(
            [JSON.stringify(metadata)],
            {type: RequestHeader.JSON_CONTENT_TYPE}
        );
        formData.append('data', metadataBlob);

        return formData;
    }

    const handleSaveResourceButton = () => {
        module.saveResourceButton.on('click', function () {
            const resourceParams = getResourceRequestParams();
            console.log(resourceParams)
            $.ajax({
                url: API_ADMIN_RESOURCE,
                type: "POST",
                data: resourceParams,
                processData: false,
                contentType: false,
            })
                .done(() => {
                    SweetAlert.showAlert('success', createSuccess, '');
                    module.createResourceModalSelector.modal('hide');
                })
                .fail((jqXHR) => {
                    App.handleResponseMessageByStatusCode(jqXHR);
                    FormHandler.handleServerValidationError(module.createResourceFormSelector, jqXHR)
                })
        });
    }

    return module;
})();
