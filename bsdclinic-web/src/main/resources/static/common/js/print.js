export const Printer = (function () {
    const module = {};

    module.openPrintWindow = (content, title) => {
        const printWindow = window.open('', '_blank');
        printWindow.document.write(`
            <html lang="">
                <head>
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">
                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js" integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q" crossorigin="anonymous"></script>
                    <title>${title}</title>
                    <style>
                        body { 
                            font-family: Arial, sans-serif; 
                            padding: 20px;
                            background: white;
                            line-height: 1.6;
                            color: #333;
                        }
                        
                        .clinic-logo {
                            width: 40px;
                            height: 40px;
                        }
                        
                        .container-fluid {
                            max-width: 100%;
                            margin: 0 auto;
                        }
                        
                        table {
                            width: 100%;
                            border-collapse: collapse;
                            margin-bottom: 20px;
                        }
                        
                        table, th, td {
                            border: 1px solid #ddd;
                        }
                        
                        th, td {
                            padding: 8px 12px;
                            text-align: left;
                        }
                        
                        th {
                            background-color: #f5f5f5;
                            font-weight: bold;
                        }
                        
                        h1, h2, h3, h4, h5, h6 {
                            margin-top: 0;
                            margin-bottom: 15px;
                        }
                        
                        p {
                            margin-bottom: 10px;
                        }
                        
                        @media print {
                            body { 
                                margin: 0; 
                                padding: 15px; 
                                font-size: 12px;
                            }
                            
                            .no-print { 
                                display: none !important; 
                            }
                            
                            table {
                                page-break-inside: avoid;
                            }
                            
                            h1, h2, h3, h4, h5, h6 {
                                page-break-after: avoid;
                            }
                        }
                    </style>
                </head>
                <body>
                    <div class="container-fluid">
                        ${content}
                    </div>
                </body>
            </html>
        `);

        printWindow.document.close();
        printWindow.focus();

        $(printWindow).on('load', function() {
            printWindow.print();
            printWindow.close();
        });
    }

    return module;
})();